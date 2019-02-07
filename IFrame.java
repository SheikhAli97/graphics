/**
 * IFrame.java
 *
 * Created: Time-stamp: <2019-01-10 15:52:48 rlc3>
 *
 * @author <a href="mailto:">Roy L. Crole</a>
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class IFrame extends JFrame implements MouseListener, MouseMotionListener{

    private IMTCanvas canvas;
    private JComboBox box;
    private JCheckBox checkBox;
    private String[] options ={"linearBox","linearOct","phaseShift"};
    private JSpinner sp;

    public final int size=60;

    // IFrame constructor
    public IFrame() {

        // Setting for the Frame
        setTitle("Image Manipulation Tool");
        setSize(850,850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container cp = getContentPane();
        cp.setLayout(null);

        // creating and adding the canvas to the frame
        canvas= new IMTCanvas();
        canvas.setBounds(0,0,600,600);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        cp.add(canvas);

        // creating and adding the drop box to the frame
        box = new JComboBox(options);
        box.setBounds(650,50,150,50);
        cp.add(box);

        // creating and adding the check box to the frame
        checkBox= new JCheckBox( "active on movement");
        checkBox.setBounds(650,150,170,50);
        cp.add(checkBox);

        sp = new JSpinner();
        sp.setBounds(650,200,40,20);
        cp.add(sp);

    } // end IFrame

    // --- begin mouse listener methods

    public void	mouseClicked(MouseEvent e) {
        int parameter = Integer.parseInt(sp.getValue().toString());

        int answer=box.getSelectedIndex();
        switch (answer) {
            case 0 :
                ImageManipulation.linearBox(canvas.getImage(), parameter,
                        e.getX(),e.getY(), size);
                canvas.reset();
                break;
            case 1 :
                ImageManipulation.linearOct(canvas.getImage(), parameter,
                        e.getX(),e.getY(), size);
                canvas.reset();
                break;
            case 2 :
                ImageManipulation.phaseShift(canvas.getImage(), parameter,
                        e.getX(),e.getY(), size);
                canvas.reset();
                break;
        }

        canvas.repaint();

    } // end mouseClicked

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e){}

    public void mouseMoved(MouseEvent e) {
        if (checkBox.isSelected()) {
            int parameter = Integer.parseInt( sp.getValue().toString());
            int answer=box.getSelectedIndex();
            switch (answer) {
                case 0 :
                    BufferedImage temp=canvas.getTempImage();
                    ImageManipulation.linearBox(temp, parameter,
                            e.getX(),e.getY(), size);
                    break;
                case 1 :
                    temp=canvas.getTempImage();
                    ImageManipulation.linearOct(temp, parameter,
                            e.getX(),e.getY(), size);
                    break;
                case 2 :
                    temp=canvas.getTempImage();
                    ImageManipulation.phaseShift(temp, parameter,
                            e.getX(),e.getY(), size);
                    break;

            }
            canvas.repaint();
        }
    } // end mouseMoved

    // --- end mouse listener methods

    // main method
    public static void main(String[] args) {

        IFrame i= new IFrame();
        i.setVisible(true);
    }

} // IFrame
