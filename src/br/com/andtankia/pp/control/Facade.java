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
            Elements links = d.getElementsByTag("link");
            links = links != null ? links : new Elements();
            sb.delete(0, sb.length());
            l.info(sb.append("Total number of link tags: ").append(links.size()).toString());
            for (Element element : links) {
                element.removeAttr("crossorigin");
                element.removeAttr("integrity");
                String href = element.attr("href");
                if (href != null && href.endsWith(".css")) {
                    GenericResource r = new GenericResource();
                    r.setOriginalUrl(href);
                    r.setExtention(".css");
                    r.setName(href.replace("/", "dash").replace("\\", "dash").replace(":", "colon"));
                    r.setLocation("css/");
                    sb.delete(0, sb.length());
                    r.setUrl(sb.append(r.getLocation()).append(r.getName()).toString());
                    p.getCsss().add(r);
                    sb.delete(0, sb.length());
                    l.info(sb.append("Registering css resource ").append(href).toString());
                }
            }

            /*Treatment when element is script*/
            Elements scripts = d.getElementsByTag("script");
            scripts = scripts != null ? scripts : new Elements();
            sb.delete(0, sb.length());
            l.info(sb.append("Total number of script tags: ").append(scripts.size()).toString());
            for (Element script : scripts) {
                script.removeAttr("crossorigin");
                script.removeAttr("integrity");
                String src = script.attr("src");

                if (src != null && src.endsWith(".js")) {
                    GenericResource r = new GenericResource();
                    r.setOriginalUrl(src);
                    r.setExtention(".js");
                    r.setName(src.replace("/", "dash").replace("\\", "dash").replace(":", "colon"));
                    r.setLocation("js/");
                    sb.delete(0, sb.length());
                    r.setUrl(sb.append(r.getLocation()).append(r.getName()).toString());
                    p.getJss().add(r);
                    sb.delete(0, sb.length());
                    l.info(sb.append("Registering JS resource ").append(src).toString());
                }
            }

            /*Treatment when element is image*/
            Elements imgs = d.getElementsByTag("img");
            imgs = imgs != null ? imgs : new Elements();
            sb.delete(0, sb.length());
            l.info(sb.append("Total number of img tags: ").append(imgs.size()).toString());
            for (Element img : imgs) {
                img.removeAttr("crossorigin");
                img.removeAttr("integrity");
                String src = img.attr("src");

                if (src != null && (src.endsWith(".jpg") || src.endsWith(".png") || src.endsWith(".gif"))) {
                    GenericResource r = new GenericResource();
                    r.setOriginalUrl(src);
                    r.setExtention(src.substring(src.length() - 4, src.length()));
                    r.setName(src.replace("/", "dash").replace("\\", "dash").replace(":", "colon"));
                    r.setLocation("images/");
                    sb.delete(0, sb.length());
                    r.setUrl(sb.append(r.getLocation()).append(r.getName()).toString());
                    p.getImages().add(r);
                    sb.delete(0, sb.length());
                    l.info(sb.append("Registering image resource ").append(src).toString());
                }
            }

            sb.delete(0, sb.length());
            l.info(sb.append("Total number of csss files read: ").append(p.getCsss().size()).toString());
            sb.delete(0, sb.length());
            l.info(sb.append("Total number of jss files read: ").append(p.getJss().size()).toString());
            sb.delete(0, sb.length());
            l.info(sb.append("Total number of images files read: ").append(p.getImages().size()).toString());

            /*GET THE PAGE CONTENT*/
            p.setContent(d.toString());

            /*JUST TO REPLACE ORIGINAL URLS BY SYMBOLIC URLS*/
            sb.delete(0, sb.length());

            l.info(sb.append("Process to replace original urls to created symbolic urls..").toString());
            for (Element link : links) {
                for (Object css : p.getCsss()) {
                    GenericResource gcss = (GenericResource) css;
                    if (link.attr("href").equals(gcss.getOriginalUrl())) {
                        sb.delete(0, sb.length());
                        l.info(sb.append("Replacing old css href ").append(gcss.getOriginalUrl()).append(" to ")
                                .append(gcss.getUrl()).toString());
                        p.setContent(p.getContent().replace(gcss.getOriginalUrl(), gcss.getUrl()));
                    }
                }
            }
            for (Element script : scripts) {
                for (Object js : p.getJss()) {
                    GenericResource gjs = (GenericResource) js;
                    if (script.attr("src").equals(gjs.getOriginalUrl())) {
                        sb.delete(0, sb.length());
                        l.info(sb.append("Replacing old js src ").append(gjs.getOriginalUrl()).append(" to ")
                                .append(gjs.getUrl()).toString());
                        p.setContent(p.getContent().replace(gjs.getOriginalUrl(), gjs.getUrl()));
                    }
                }
            }
            for (Element img : imgs) {
                for (Object pic : p.getImages()) {
                    GenericResource gpic = (GenericResource) pic;
                    if (img.attr("src").equals(gpic.getOriginalUrl())) {
                        sb.delete(0, sb.length());
                        l.info(sb.append("Replacing old img src ").append(gpic.getOriginalUrl()).append(" to ")
                                .append(gpic.getUrl()).toString());
                        p.setContent(p.getContent().replace(gpic.getOriginalUrl(), gpic.getUrl()));
                    }
                }
            }
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
}
