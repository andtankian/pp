package br.com.andtankia.pp.control;

import br.com.andtankia.pp.domain.AbstractResource;
import br.com.andtankia.pp.domain.GenericResource;
import br.com.andtankia.pp.domain.Page;
import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.dto.Result;
import br.com.andtankia.pp.rules.ICommand;
import br.com.andtankia.pp.rules.ValidateProjectNameCommand;
import br.com.andtankia.pp.rules.ValidateURLCommand;
import br.com.andtankia.pp.rules.VerifyVersionCommand;
import br.com.andtankia.pp.utils.CLog;
import br.com.andtankia.pp.utils.PPArguments;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author andrew
 */
public class Facade {

    FlowContainer fc;
    Logger l;
    StringBuilder sb;

    public Facade(FlowContainer fc) {
        this.fc = fc;
        fc.setResult(new Result());
        fc.setProceed(true);
        sb = new StringBuilder();
    }

    public void process() {

        try {
            runBefore();
            run();
            runAfter();
        } catch (Exception e) {
            System.out.println(e);
        }

        l = l == null ? CLog.getCLog("version") : l;
        l.info(fc.getResult().toString());
    }

    private void runBefore() throws Exception {
        List l = new ArrayList();
        l.add(new VerifyVersionCommand());
        l.add(new ValidateProjectNameCommand());
        l.add(new ValidateURLCommand());

        for (Object object : l) {
            ((ICommand) object).exe(fc);
            mustProceed();
        }
    }

    private void runAfter() {

    }

    private void run() throws IOException {

        l = CLog.getCLog();
        PPArguments ppa = fc.getPpholder().getPpa();
        /*If it gets here, we are ready to get content from a website page*/
        processPage(registerPage(ppa.getUrl()));

    }

    private void mustProceed() throws Exception {
        if (!fc.isProceed()) {
            throw new Exception(fc.getResult().getMessage().getError());
        }
    }

    private Page registerPage(String pageUrl) throws IOException {
        Page p = new Page();
        if (pageUrl.endsWith(".js")
                || pageUrl.endsWith(".css")
                || pageUrl.endsWith(".jpg")
                || pageUrl.endsWith(".png")
                || pageUrl.endsWith(".jpeg")
                || pageUrl.endsWith(".gif")) {
        } else {
            l.info(sb.append("Registering page resource: ").append(pageUrl).append("...").toString());
            Document d = Jsoup.connect(pageUrl).get();
            /*PROBABLY LOADED CORRECTLY, SO THERE IS A PAGE RESOUCE AVAILABLE*/
            sb.delete(0, sb.length());
            l.info(sb.append("Getting page resource ").append(pageUrl).append(" content").toString());
            p.setOriginalUrl(pageUrl);
            p.setName(pageUrl.replace("/", "dash").replace("\\", "dash").replace(":", "colon"));
            sb.delete(0, sb.length());
            p.setLocation(sb.append(fc.getPpholder().getProject().getLocation()).append(File.separator).toString());
            p.setExtention(".html");
            sb.delete(0, sb.length());
            l.info(sb.append("Getting links tag of resource ").append(pageUrl).toString());

            /*Treatment when elements are links (probably css)*/
            extractStringInformationFromTags(p, d.getElementsByTag("link"));

            /*Treatment when element is script*/
            extractStringInformationFromTags(p, d.getElementsByTag("script"));

            /*Treatment when element is image*/
            extractStringInformationFromTags(p, d.getElementsByTag("img"));
            
            /*TO WRITE CONTENT RECOVERED FROM PAGE*/
            p.setContent(d.toString());
            
            /*ADJUST LINKS AND TAGS OF THE PAGE CONTENT*/
            replaceFromPage(p, d.getElementsByTag("link"));
            replaceFromPage(p, d.getElementsByTag("script"));
            replaceFromPage(p, d.getElementsByTag("img"));
            
            
        }

        return p;
    }

    private void processPage(Page page) {
        sb.delete(0, sb.length());
        sb.append(page.getLocation()).append(page.getName()).append(page.getExtention());
        File f = new File(sb.toString());

        /*Firstly, write the page resource to a file.*/
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ioe) {
                l.severe("Error while trying to create new page resource..\nProgram will be aborted.");
            }
            try {
                FileOutputStream fos = new FileOutputStream(f);

                sb.delete(0, sb.length());
                l.info(sb.append("Writing page resource ").append(page.getName()).append(" to a file..").toString());
                fos.write(page.getContent().getBytes());

                fos.flush();
                fos.close();
            } catch (IOException ex) {
                l.severe("Error while trying to write new page resource..\nProgram will be aborted.");
            }
        }

        /*Now, compare if children resource like css, js and images exists, 
        if does, ignore, if doesn't create a new and write.
        
        Preparing css folders*/
        prepareFolders(page.getCsss(), "css");
        /*Preparing js folders*/
        prepareFolders(page.getJss(), "js");
        /*Preparing image folders*/
        prepareFolders(page.getImages(), "images");

        /*We need to create and write all available resources
        
        Write css resources*/
        writeResource(page, fc.getPpholder().getProject().getAllcsss(), "css");
        /*Write js resources*/
        writeResource(page, fc.getPpholder().getProject().getAlljsss(), "js");
        /*Write images resources*/
        writeResource(page, fc.getPpholder().getProject().getAllimages(), "image");

    }

    /**
     * *
     * Method to prepare the folders when a project has many resources as css,
     * js and images.
     *
     * @param resources
     * @param type
     */
    private void prepareFolders(List resources, String type) {
        sb = sb != null ? sb : new StringBuilder();
        if (resources != null && !resources.isEmpty()) {
            sb.delete(0, sb.length());
            File resourceFolders = new File(sb.append(fc.getPpholder().getProject().getLocation()).append(File.separator).append(type).toString());
            resourceFolders.mkdir();
        }
    }

    /**
     * Method to write an generic resource
     *
     * @param page
     * @param projectResourcesToCompare
     * @param type
     */
    private void writeResource(Page page, List projectResourcesToCompare, String type) {
        boolean processing = true;
        List resources = null;
        FileOutputStream fos;
        if (type.equals("js")) {
            resources = page.getJss();
        } else if (type.equals("css")) {
            resources = page.getCsss();
        } else if (type.equals("image")) {
            resources = page.getImages();
        }
        for (Object resource : resources) {
            AbstractResource aresource = (AbstractResource) resource;
            for (Object prtc : projectResourcesToCompare) {
                if (aresource.getName().equals(prtc)) {
                    processing = false;
                    break;
                }
            }

            if (processing) {
                sb.delete(0, sb.length());
                /*Verify if original url isn't an absolute URL, if it isn't, 
                        so add the host to the start of url.*/
                if (!((GenericResource) aresource).getOriginalUrl().startsWith("https://")
                        && !((GenericResource) aresource).getOriginalUrl().startsWith("http://")) {
                    boolean hasEndingDash = false;
                    if (fc.getPpholder().getProject().getBaseIndex().endsWith("/")) {
                        fc.getPpholder().getProject().setBaseIndex(fc.getPpholder().getProject().getBaseIndex().substring(0, fc.getPpholder().getProject().getBaseIndex().length() - 1));
                    }
                    ((GenericResource) aresource).setOriginalUrl(sb.append(fc.getPpholder().getProject().getBaseIndex()).append("/")
                            .append(((GenericResource) aresource).getOriginalUrl()).toString());
                }
                sb.delete(0, sb.length());
                l.info(sb.append("Recovering content of ").append(type).append(" ").append(aresource.getName()).append("...").toString());

                /*Execute this block if resource is a CSS resource*/
                if (type.equals("css")) {
                    try {
                        Document cssItSelf = Jsoup.connect(((GenericResource) aresource).getOriginalUrl()).get();
                        aresource.setContent(cssItSelf.body().text());
                        sb.delete(0, sb.length());
                        File cssFile = new File(sb.append(fc.getPpholder().getProject().getLocation()).append(File.separator).append(aresource.getUrl()).toString());
                        cssFile.createNewFile();
                        fos = new FileOutputStream(cssFile);
                        sb.delete(0, sb.length());
                        l.info(sb.append("Creating and writing css resource ").append(aresource.getName()).append("...").toString());
                        fos.write(aresource.getContent().getBytes());
                        fos.flush();
                        fos.close();
                        fc.getPpholder().getProject().getAllcsss().add(aresource.getName());
                    } catch (IOException io) {
                        sb.delete(0, sb.length());
                        l.warning(sb.append("An error ").append(io.getMessage()).append(" occurred while writing css resource ").append(aresource.getName()).toString());
                    }
                } else if (type.equals("js")) {
                    /*Execute this block when resource is a JS file*/
                    try {
                        sb.delete(0, sb.length());
                        l.info(sb.append("Recovering content of js file ").append(aresource.getName()).append("...").toString());

                        URL jsfileURL = new URL(((GenericResource) aresource).getOriginalUrl());
                        URLConnection jsfileURLConnection = jsfileURL.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                jsfileURLConnection.getInputStream()));
                        String inputLine;
                        String finalLine = "";
                        while ((inputLine = in.readLine()) != null) {
                            finalLine += inputLine + "\n";
                        }
                        in.close();
                        aresource.setContent(finalLine);
                        sb.delete(0, sb.length());
                        File jsFile = new File(sb.append(fc.getPpholder().getProject().getLocation()).append(File.separator).append(aresource.getUrl()).toString());
                        jsFile.createNewFile();
                        fos = new FileOutputStream(jsFile);
                        sb.delete(0, sb.length());
                        l.info(sb.append("Creating and writing js resource ").append(aresource.getName()).append("...").toString());
                        fos.write(aresource.getContent().getBytes());
                        fos.flush();
                        fos.close();
                        fc.getPpholder().getProject().getAlljsss().add(aresource.getName());
                    } catch (IOException io) {
                        sb.delete(0, sb.length());
                        l.warning(sb.append("An error ").append(io.getMessage()).append(" occurred while writing js resource ").append(aresource.getName()).toString());
                    }
                } else if (type.equals("image")) {

                    /*Execute this block when its an image resource*/
                    try {
                        sb.delete(0, sb.length());
                        l.info(sb.append("Recovering content of image file ").append(aresource.getName()).append("...").toString());

                        BufferedImage image = null;
                        URL url = new URL(((GenericResource) aresource).getOriginalUrl());
                        image = ImageIO.read(url);
                        sb.delete(0, sb.length());
                        File imgFile = new File(sb.append(fc.getPpholder().getProject().getLocation()).append(File.separator).append(aresource.getUrl()).toString());
                        imgFile.createNewFile();
                        sb.delete(0, sb.length());
                        l.info(sb.append("Creating and writing image resource ").append(aresource.getName()).append("...").toString());
                        ImageIO.write(image, aresource.getExtention().replace(".", ""), imgFile);
                        fc.getPpholder().getProject().getAllimages().add(aresource.getName());
                    } catch (IOException io) {
                        sb.delete(0, sb.length());
                        l.warning(sb.append("An error ").append(io.getMessage()).append(" occurred while writing image resource ").append(aresource.getName()).toString());
                    }
                }
            }
            processing = true;
        }
    }

    private void extractStringInformationFromTags(Page p, List elements) {
        if (elements != null && !elements.isEmpty()) {
            Elements _elements = (Elements) elements;
            String tn = _elements.get(0).tagName();
            String extension, location, type, attr, link;
            String[] extensions = null;
            List resource = null;
            extension = location = type = attr = link = null;
            for (Element _element : _elements) {
                _element.removeAttr("crossorigin");
                _element.removeAttr("integrity");
                if (tn.equals("link")) {
                    extension = ".css";
                    extensions = new String[]{".css"};
                    location = "css/";
                    type = "css";
                    attr = "href";
                    resource = p.getCsss();
                } else if (tn.equals("script")) {
                    extension = ".js";
                    extensions = new String[]{".js"};
                    location = "js/";
                    type = "js";
                    attr = "src";
                    resource = p.getJss();
                } else if (tn.equals("img")) {
                    extensions = new String[]{".jpg", ".png", ".gif", ".jpeg"};
                    location = "images/";
                    type = "image";
                    attr = "src";
                    resource = p.getImages();
                }

                link = _element.attr(attr);

                /*Little trick when tag link has more then one extension, like images*/
                boolean validExtension = false;
                for (Object ext : extensions) {
                    String ex = (String) ext;
                    if (link.endsWith(ex)) {
                        extension = ex;
                        validExtension = true;
                        break;
                    }
                }
                if (link != null && validExtension) {
                    GenericResource r = new GenericResource();
                    r.setOriginalUrl(link);
                    r.setExtention(extension);
                    r.setName(link.replace("/", "dash").replace("\\", "dash").replace(":", "colon"));
                    r.setLocation(location);
                    sb.delete(0, sb.length());
                    r.setUrl(sb.append(r.getLocation()).append(r.getName()).toString());
                    resource.add(r);
                    sb.delete(0, sb.length());
                    l.info(sb.append("Registering ").append(type).append(" resource: ")
                            .append(r.getOriginalUrl()).toString());
                }

            }
            
            sb.delete(0, sb.length());
        l.info(sb.append("Total number of ").append(type).append(" files read: ").append(elements.size()).toString());

        }
    }

    private void replaceFromPage(Page p, List elements) {
        Elements _elements = (Elements) elements;

        if (_elements != null && !_elements.isEmpty()) {
            List resource = null;
            String attr = "";
            sb.delete(0, sb.length());
            for (Element _element : _elements) {
                if(_element.tagName().equals("script")){
                    resource = p.getJss();
                    attr = "src";
                } else if(_element.tagName().equals("link")){
                    resource = p.getCsss();
                    attr = "href";
                } else if(_element.tagName().equals("img")){
                    resource = p.getImages();
                    attr = "src";
                }
                for (Object asset : resource) {
                    GenericResource gr = (GenericResource) asset;
                    if (_element.attr(attr).equals(gr.getOriginalUrl())) {
                        sb.delete(0, sb.length());
                        l.info(sb.append("Replacing old resource ").append(attr).append(" ").append(gr.getOriginalUrl()).append(" to ")
                                .append(gr.getUrl()).toString());
                        p.setContent(p.getContent().replace(gr.getOriginalUrl(), gr.getUrl()));
                        break;
                    }
                }
            }
        }
    }
}
