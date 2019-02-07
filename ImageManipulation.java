/**
 * Time-stamp: <2019-01-10 16:31:16 rlc3>
 * <p>
 * ImageManipulation.java
 * <p>
 * Class allows the manipulation of an image by
 * three alternative methods.
 *
 * @author Roy Crole
 */

import java.awt.image.*;
import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;

public class ImageManipulation {

// ----- template code commented out BEGIN

    // linear transformation to compute prei from i
    // O D and P are points on a line with i between D and P
    static int linTrans (int O, int i, int D, int P) {
        double a = D-P;
        double b = D-O;
        double m = a/b;
        double k = D - D*m;
        int prei = (int) (i*m + k);
        return prei;
    }

    static public void linearBox(BufferedImage image, int n, int x, int y, int size) {

        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        (temp.getGraphics()).drawImage(image, 0, 0, image.getWidth(), image.getHeight(),null);

        if((MouseInfo.getPointerInfo().getLocation().x<image.getWidth())&&(MouseInfo.getPointerInfo().getLocation().y<image.getHeight())){
            for (int i=x-size; i<=x+size; i++) {
                for (int j=y-size; j<=y+size; j++){

                    int O = x-size;
                    int P = x+size;
                    int D = x+n;

                    if(i>O&&i<D){
                        int prei = linTrans(O, i, D, P);
                        image.setRGB(i, j, temp.getRGB(prei, j));
                    }
                    else{
                        image.setRGB(i,j,0xaaaaaa);
                    }
                }
            }
        }



    static public void phaseShift(BufferedImage image, int n, int x, int y, int size) {

        // creates a copy of the image called temp
        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        (temp.getGraphics()).drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

        for (int i = x - size; i < Math.min(image.getWidth() - (2 * n), x + size); i++) {
            for (int j = y - size; j < Math.min(image.getHeight() - (2 * n), y + size); j++) {

                if (i >= 300) {
                    image.setRGB(i, j, 0xff);
                } else {

                    int colorAtTop = temp.getRGB(i, j + (2 * n));

                    // extract green from the colorAtTop.
                    int green = (colorAtTop >> 8) & 0xFF;

                    // the new color replacing all rgb with green extracted.
                    int newColor = (green << 16) | (green << 8) | (green);

                    image.setRGB(i, j, newColor);

                }


            }
        }

    }

// ---- END phaseShift

    // / ---- BEGIN linearOct

    static int[] octlinTrans(int O, int D, int I, int J, int size) {

        // we will compute preI and preJ and return them in pre
        int[] pre = new int[2];

        // compute d from I and J
        double d = Math.sqrt((I * I) + (J * J));

        // calculate P (from theta, itself from I and J)
        double theta = Math.atan((double) J / (double) I);
        double p = size / Math.cos(theta);

        // compute pred from O, D, d, p
        double pred = linTrans(O, (double) (size / 3), d, p);

        // now compute pre ....
        int newIValue = (int) (pred * Math.cos(theta));
        int newJValue = (int) (pred * Math.sin(theta));

        pre[0] = newIValue;
        pre[1] = newJValue;

        return pre;

    } // end octlinTrans

    static public void linearOct(BufferedImage image, int n, int x, int y, int size) {

        BufferedImage temp=new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        (temp.getGraphics()).drawImage(image, 0, 0, image.getWidth(), image.getHeight(),null);

        // ----- template code commented out BEGIN

        // check if A(x,y) lies within image
        if (x + size < image.getWidth() && x - size >= 0 && y + size < image.getHeight() && y - size >= 0) {
            // loop visiting each pixel in A(x,y) at image coordinate (i,j)
            for (int i=x-size; i<= x+size; i++) {
                for(int j=y-size/2; j<=y+size; j++) {
                    // Apply IMGTRANS to each line ODP specified by an (i,j):

                    // a list to store preI and preJ as element 0 and 1
                    // pre is calculated below using octlinTrans
                    int [] pre = new int[2];
                    // convert image coordinates (i,j) to cartesian coordinates
                    //  .... for example mouse position (x,y) is converted to (x,-y)
                    // then move the mouse position to the origin (0,0) and
                    // ... translate all other positions relatively ...
                    // so that you now work with A(0,0) in cartesian coordinates
                    // I and J below are relative cartesian coordinates
                    // note: Cart Coord -j moves up (ie - ) by an amount -y
                    int I = i - x;
                    int J = -j + y;

                    // set d = distance of origin to (I,J)
                    int d = (int) Math.sqrt((I * I) + (J * J));

                    // if (I,J) is outside the circle of radius size/3
                    // then we compute (preI, preJ) from (I,J) using octlinTrans
                    int radius = size / 3;
                    if (d > radius) { // radius test
                        // perform linear transformation in octant OGV
                        // 0 < J < I
                        if (0 < J && J < I)
                        {
                            pre = octlinTrans(0, d, I, J, size); // use octlinTrans
                        }
                        // perform linear transformation in octant OVH
                        // 0 < -J < I
                        else if (0 < -J && -J < I)
                        {
                            //mirror in x (I) axis
                            J = -J;

                            pre = octlinTrans(n, d, I, J, size);

                            //mirror in x (I) axis
                            J = -J;
                            pre[1] = -pre[1];
                        }
                        // perform linear transformation in octant OKU
                        else if(0 < -J && -J < -I)
                        {
                            //mirror in line y = x
                            J = -J;
                            I = -I;

                            pre = octlinTrans(n, d, I, J, size);

                            //mirror in line y = x
                            J = -J;
                            I = -I;
                            pre[0] = -pre[0];
                            pre[1] = -pre[1];


                        }
                        // perform linear transformation in octant OUF
                        else if(0 < J && J < -I)
                        {
                            //mirror in y (J) axis
                            I = -I;

                            pre = octlinTrans(n, d, I, J, size);

                            I = -I;
                            pre[0] = -pre[0];
                        }
                        // identity transformation elsewhere (outside the circle)
                        else {
                            pre[0] = I;
                            pre[1] = J;
                        } // end nested if statements

                        // transform relative cartesian coordinate (preI,preJ)
                        // back to image coordinate (prei,prej)
                        /*
                         * reverse this:
                         * int I = i - x;
                         * int J = -j + y;
                         */
                        pre[0] += x;
                        pre[1] = y - pre[1];

                        // set RGB of pixel at (i,j) to RGB from (prei,prej)
                        image.setRGB(i, j, temp.getRGB(pre[0], pre[1]));

                    } // matches radius test
                    else {
                        image.setRGB(i, j, 0xaaaaaa);
                    } // end if

                } // end forLoop j
            } // end forLoop i
        } // end check that A(x,y) is in image

        //----- template code commented out END

    } // end method linearOct


}
