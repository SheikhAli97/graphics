/**
 * IMTCanvas.java
 *
 * Created: Time-stamp: <2019-01-10 15:52:38 rlc3>
 *
 * @author <a href="mailto:">R. Crole</a>
 * @version 1.0
 */

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class IMTCanvas extends Canvas {

    private BufferedImage i, ti,tti ;

    public IMTCanvas() {

        try{

            File f = new File("picture.jpg");
            i = ImageIO.read(f).getSubimage(0,0,600,600);
            ti = ImageIO.read(f).getSubimage(0,0,600,600);
            tti = ImageIO.read(f).getSubimage(0,0,600,600);
        }

        catch (Exception e) {System.out.println(e.getMessage());}
        reset();

    } // IMTCanvas constructor

    public void paint (Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        g.drawImage(ti,0,0,600,600,this);
    }

    public BufferedImage getImage() {
        return i;
    }

    public BufferedImage getTempImage() {
        (tti.getGraphics()).drawImage(i,0,0,600,600,this);
        return tti;
    }

    public void reset() {
        ti=getTempImage();
    }

    public void setTempImage(BufferedImage b) {
        ti=b;
    }

} // IMTCanvas
