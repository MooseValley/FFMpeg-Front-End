/*
 Author: Mike O'Malley
 Source File: FFMpegFrontEnd.java
 Description: TBA ... :)

Ammendment History
Ver   Date        Author    Details
----- ----------- --------  ----------------------------------------------------
0.01  01-May-2021  Mike O   Created.

*/
import java.util.Formatter;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.FormatterClosedException;
import java.io.EOFException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;     // For DecimalFormat.
import java.text.SimpleDateFormat;  // For SimpleDateFormat support.
import java.util.Date;              // For Date support.
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;     // For Swing Dialogs
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Window;             // For isAlwaysOnTopSupported(), setAlwaysOnTop (), etc.
import java.awt.Toolkit;            // For Copy to Clipboard
import java.awt.datatransfer.Clipboard;  // For Copy to Clipboard
import java.awt.datatransfer.StringSelection;  // For Copy to Clipboard
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.datatransfer.Transferable;
//import java.awt.dnd.*;
import java.awt.dnd.DropTarget;  // Drag and Drop
import java.awt.dnd.DropTargetListener; // Drag and Drop
import java.awt.dnd.DropTargetDragEvent; // Drag and Drop
import java.awt.dnd.DropTargetEvent; // Drag and Drop
import java.awt.dnd.DropTargetDropEvent; // Drag and Drop
import java.awt.dnd.DnDConstants; // Drag and Drop
import java.io.FileReader; // Drag and Drop
import java.io.File; // Drag and Drop
import java.io.BufferedReader;

import java.util.Scanner;
import java.util.StringTokenizer;

//import java.util.LocalDate;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Arrays;
import java.util.Collections;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JComponent;


public class FFMpegFrontEnd extends JFrame
{
   // *** CONSTANTS:
   private static final String APPLICATION_VERSION          = "v0.19";
   private static final String APPLICATION_TITLE            = "FFMpeg Front End � " + APPLICATION_VERSION;
   private static final String APPLICATION_AUTHOR           = "Mike O'Malley";
   private static final String APP_NAME_VERSION_AUTHOR      = APPLICATION_TITLE;//+ " - by " + APPLICATION_AUTHOR;
   private static final int    FRAME_WIDTH                  = 950;
   private static final int    FRAME_HEIGHT                 = 550;
   private static final String INI_FILE_NAME                = "FFMpegFrontEnd.ini";
   private static final SimpleDateFormat CENTURY_FORMAT     = new SimpleDateFormat ("yyyy");
   private static final SimpleDateFormat DD_MMM_YYYY_FORMAT = new SimpleDateFormat ("dd-MMM-yyyy");
   private static final String DELIMITER                    = ",";
   private static final String LOG_FILE                     = "FFMpegFrontEnd_log.txt";

   private static final String DOWNLOAD_BAT_FILE            = "00_download_youtube_files.bat";
   private static final String SUPPORTED_INPUT_FILE_TYPES   = "MP4,AVI,MOV,FLV,M4V,MKV,MPG,ASF,TS,MPEG,WMV,VOB,";
   private static final String[] SUPPORTED_INPUT_FILE_TYPES_ARRAY   = SUPPORTED_INPUT_FILE_TYPES.split(",");
   private static final String DEFAULT_INPUT_VIDEO_FOLDER   = "C:\\000 - TEMP\\"; // "c:\\Camtasia\\";


   private static final String YOUTUBEDL_COMMAND_LINE_DEFAULT_VIDEO_STR = "youtube-dl.exe ";
   private static final String YOUTUBEDL_COMMAND_LINE_MP4_VIDEO_STR     = "youtube-dl.exe -f mp4 ";
   private static final String YOUTUBEDL_COMMAND_LINE_MP3_AUDIO_STR     = "youtube-dl.exe -x --audio-format mp3 ";


   // Icons setup: do this *BEFORE* any icons are added to any buttons (or they will not show up).
   private Icons       aIcons = new Icons ();  // Run the constructor to load in the icon files.

   // *** GUI Components:
   private JTabbedPane jTabbedPane                     = new JTabbedPane  ();
   private JLabel      applicationTitleLabel           = new JLabel       (" " + APPLICATION_TITLE + " ");
   private JLabel      applicationAuthorLabel          = new JLabel       (""); //"          by " + APPLICATION_AUTHOR + " ");
   private JTextField  folderPathTextField             = new JTextField   (60);
   private JButton     listFilesButton                 = new JButton      ("List Files", Icons.paste_clipboard_icon);
 //private JButton     pasteMP4VideoButton             = new JButton      ("Paste URLs MP4",           Icons.paste_clipboard_icon);
 //private JButton     pasteMP3AudioButton             = new JButton      ("Paste URLs MP3 Audio",     Icons.paste_clipboard_icon);
   private JButton     generateMP4FFBatButton               = new JButton      ("Generate MP4 FF BAT",           Icons.file_save_icon);
   private JButton     listNonVideoFilesButton              = new JButton      ("List Non-Video FIles",          Icons.report_icon);
   private JButton     generateDeleteRedundantMp4BatButton  = new JButton      ("Generate Delete Redundant MP4 BAT",           Icons.file_save_icon);
   private JButton     generateFFMp4FileSizeChangePercentButton  = new JButton      ("Generate File Change%",           Icons.file_save_icon);
 //private JButton     deleteBATFileButton             = new JButton      ("Reset",    Icons.trash_garbage_icon);
 //private JButton     appendBATFileButton             = new JButton      ("Append to Download Commands", Icons.file_save_icon);
   private JButton     aboutButton                     = new JButton      ("Help",     Icons.help_icon);
   private JButton     exitButton                      = new JButton      ("Exit",     Icons.exit_icon);

   private JTextArea   sourceLinesTextArea             = new JTextArea    ();
   private JScrollPane sourceLinesTextAreaScrollPane   = new JScrollPane  (sourceLinesTextArea);
   private JTextArea   resultsTextArea                 = new JTextArea    ();
   private JScrollPane resultsTextAreaScrollPane       = new JScrollPane  (resultsTextArea);
   private JRadioButton allSubDirsRadioButton          = new JRadioButton ("Dir and All Sub Dirs");
   private JRadioButton dirOnlyRadioButton             = new JRadioButton ("Dir only");

   private ArrayList<String> batchFileCommandsArrayList = new ArrayList<String> ();

   // Class data:
   private FontScalerMouseWheelListener fontScalerMouseWheelListener = new FontScalerMouseWheelListener (this);

   private ArrayList<File> filesArrayList;
   private ArrayList<File> nonVideoFilesArrayList;


   // Default Constructor:
   public FFMpegFrontEnd ()
   {
      // Set Window Title Bar text.
      super (APP_NAME_VERSION_AUTHOR);

      // *** Construct GUI ***
      setLayout (new BorderLayout());
      JPanel titlePanel      = new JPanel (new FlowLayout ());

      // Application Title.
      applicationTitleLabel.setFont(new Font("Monotype Corsiva", Font.BOLD, 36));
      titlePanel.add (applicationTitleLabel);
      titlePanel.add (applicationAuthorLabel);

      jTabbedPane.addTab ("Results",     null, createMainPanel (),         "Tooltip for Tab1");

      add (titlePanel,  BorderLayout.NORTH);
      add (jTabbedPane, BorderLayout.CENTER);


      // Specify what happens when the user clicks the "X" icon
      // (top right hand corner of JFrame).
      //setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);

      addMouseWheelListener(fontScalerMouseWheelListener);

      // Setup my own "Window Closing" method that runs when the user
      // clicks the "X" icon.  e.g. prompt user to confirm they want to exit,
      // or prompt user to see if they want to save data, etc.
      addWindowListener (new WindowAdapter()
      {
         public void windowClosing (WindowEvent e)
         {
            exitApplication ();
         }
      });

      loadApplicationSettings ();

   } // public FFMpegFrontEnd ()


   private JPanel createMainPanel ()
   {
      JPanel thePanel        = new JPanel (new BorderLayout ());
      JPanel centerPanel     = new JPanel (new GridLayout (2, 1));   // r,c
      JPanel southPanel      = new JPanel (new GridLayout (2, 1));   // r,c
      //JPanel gridPanel       = new JPanel (new GridLayout (1, 1)); // r,c
      JPanel buttonPanel     = new JPanel (new FlowLayout (FlowLayout.CENTER));
      JPanel flowPanel       = new JPanel (new FlowLayout (FlowLayout.CENTER));
      //JPanel flow3Panel      = new JPanel (new FlowLayout (FlowLayout.CENTER));
      JPanel radioPanel       = new JPanel (new FlowLayout (FlowLayout.CENTER));

      centerPanel.add (sourceLinesTextAreaScrollPane);
      centerPanel.add (resultsTextAreaScrollPane);

      ButtonGroup dirsGroup = new ButtonGroup();
      dirsGroup.add (dirOnlyRadioButton);
      dirsGroup.add (allSubDirsRadioButton);

      dirOnlyRadioButton.setSelected (true);

      radioPanel.add (dirOnlyRadioButton);
      radioPanel.add (allSubDirsRadioButton);
      radioPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));



      buttonPanel.add (generateMP4FFBatButton);
      buttonPanel.add (listNonVideoFilesButton);
      buttonPanel.add (generateDeleteRedundantMp4BatButton);
      buttonPanel.add (generateFFMp4FileSizeChangePercentButton);
      buttonPanel.add (new JLabel ("    ") );

      //buttonPanel.add (listFilesButton);
      //buttonPanel.add (pasteMP4VideoButton);
      //buttonPanel.add (pasteMP3AudioButton);
      //buttonPanel.add (new JLabel ("    ") );

      //buttonPanel.add (deleteBATFileButton);
      //buttonPanel.add (new JLabel ("    ") );

      buttonPanel.add (aboutButton);
      buttonPanel.add (exitButton);
      //buttonPanel.add (createTestFilesButton);

      //southPanel.add (folderPathTextField);
      //southPanel.add (flow2Panel);

      flowPanel.add (radioPanel);
      flowPanel.add (new JLabel ("    ") );

      flowPanel.add (new JLabel ("Video Folder: ") );
      flowPanel.add (listFilesButton);
      //flowPanel.add (pasteMP4VideoButton);
      //flowPanel.add (pasteMP3AudioButton);
      flowPanel.add (folderPathTextField);

      southPanel.add (flowPanel);
      southPanel.add (buttonPanel);

      // Add to panels / items to User Interface / main JFrame.
      thePanel.add (centerPanel,               BorderLayout.CENTER);
      //thePanel.add (resultsTextAreaScrollPane, BorderLayout.CENTER);

      thePanel.add (southPanel,                BorderLayout.SOUTH);

      listFilesButton.addActionListener                          (event -> buildVideoFilesList () );
      generateMP4FFBatButton.addActionListener                   (event -> generateMP4FFBat () );
      generateDeleteRedundantMp4BatButton.addActionListener      (event -> generateDeleteRedundantMp4Bat () );
      generateFFMp4FileSizeChangePercentButton.addActionListener (event -> generateFFMp4FileSizeChangePercent () );

      aboutButton.addActionListener             (event -> Moose_Utils.displayAboutDialog (FFMpegFrontEnd.this, APPLICATION_TITLE, APPLICATION_AUTHOR, ""));
      exitButton.addActionListener              (event -> exitApplication ());

      listNonVideoFilesButton.addActionListener (event -> listNonVideoFiles () );

      return thePanel;
   }


   // ****************************************
   // *** INNER CLASSES
   // ****************************************


   // ****************************************
   // *** METHODS
   // ****************************************

   private void exitApplication ()
   {
      int result = JOptionPane.YES_OPTION;

      /*
      if (autoSaveFileOnExitCheckBox.isSelected () == false)
      {
         result = JOptionPane.showConfirmDialog (FFMpegFrontEnd.this, "Are you sure you want to exit the application ?",
                                                 "Exit this Application ?", JOptionPane.YES_NO_OPTION);
      }
      */

      if (result == JOptionPane.YES_OPTION)
      {
         FFMpegFrontEnd.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         saveApplicationSettings ();

         System.exit(0);  // Signal that program is terminatting normally.
      }
   } // private void exitApplication ()

   private void loadApplicationSettings ()
   {
      System.out.println ("Loading ...");

      // Load application settings:
      int posX  = SaveLoadApplicationSettings.LoadIntKeyValue (INI_FILE_NAME, "posX",  0);
      int posY  = SaveLoadApplicationSettings.LoadIntKeyValue (INI_FILE_NAME, "posY",  0);
      int sizeX = SaveLoadApplicationSettings.LoadIntKeyValue (INI_FILE_NAME, "sizeX", FRAME_WIDTH);
      int sizeY = SaveLoadApplicationSettings.LoadIntKeyValue (INI_FILE_NAME, "sizeY", FRAME_HEIGHT);

      // Set position of top left hand corner of JFrame.
      // If Frame is outside of our screen bounds, then
      // Frame will be displayed in top / left corner (0, 0).
      // (This can easily happen when you are running 1 screen
      // sometimes and multiple screens at other times).
      // 95+% of apps do not check / handle this.  I do! :D
      if ((posX < 0) || (posX > Moose_Utils.getScreenWidth  ()) ||
          (posY < 0) || (posY > Moose_Utils.getScreenHeight ()))
      {
         posX = 0;
         posY = 0;
      }
      setLocation (posX, posY);

      // Set size of our JFrame.
      if ((sizeX > 0) && (sizeY > 0))
      {
         setSize (sizeX, sizeY);
      }
      else
      {
         // Use constants declared above to set the preferred JFrame size.
         setSize (FRAME_WIDTH, FRAME_HEIGHT);
      }

      float fontScale  = SaveLoadApplicationSettings.LoadFloatKeyValue (INI_FILE_NAME, "fontScale", 0.0f);
      fontScalerMouseWheelListener.setFontScale (fontScale);

      folderPathTextField.setText (SaveLoadApplicationSettings.LoadStringKeyValue
            (INI_FILE_NAME, "folderPathTextField", DEFAULT_INPUT_VIDEO_FOLDER));

      //folderPathTextField.setText ("c:\\Camtasia\\Apple 2 - APPriLE - 2021-04-17 - My Apple 2+, my First Tape, Disk, etc\\");
      //folderPathTextField.setText (DEFAULT_INPUT_VIDEO_FOLDER);

   }

   private void saveApplicationSettings ()
   {
      System.out.println ("Saving ...");

      // Save application settings: size and position of our JFrame.
      int posX = getLocation ().x;
      int posY = getLocation ().y;
      int sizeX = getSize ().width;
      int sizeY = getSize ().height;

      SaveLoadApplicationSettings.SaveIntKeyValue  (INI_FILE_NAME, "posX",  posX);
      SaveLoadApplicationSettings.SaveIntKeyValue  (INI_FILE_NAME, "posY",  posY);
      SaveLoadApplicationSettings.SaveIntKeyValue  (INI_FILE_NAME, "sizeX", sizeX);
      SaveLoadApplicationSettings.SaveIntKeyValue  (INI_FILE_NAME, "sizeY", sizeY);
      SaveLoadApplicationSettings.SaveIntKeyValue  (INI_FILE_NAME, "sizeY", sizeY);
      SaveLoadApplicationSettings.SaveStringKeyValue (INI_FILE_NAME, "folderPathTextField",
                                                      folderPathTextField.getText ());

      SaveLoadApplicationSettings.SaveFloatKeyValue  (INI_FILE_NAME,
         "fontScale", fontScalerMouseWheelListener.getFontScale ());

      SaveLoadApplicationSettings.SaveStringKeyValue
            (INI_FILE_NAME, "folderPathTextField", folderPathTextField.getText () );
   }



   // Build list of supported video files.  Ref: SUPPORTED_INPUT_FILE_TYPES
   private void buildVideoFilesList ()
   {
      // File.toString(): File name with full absolute path.
      // File.getName():  File name (no path).
      nonVideoFilesArrayList = new ArrayList<File>();

      resultsTextArea.setText("");

      System.out.println ("buildVideoFilesList (): Listing all video files: " + SUPPORTED_INPUT_FILE_TYPES +
                          " in folder: '" + folderPathTextField.getText() + "' ... ");

      if (allSubDirsRadioButton.isSelected() == true)
         filesArrayList = Moose_Utils.getAllFilesInDirecoryAndAllSubDirectories (folderPathTextField.getText() );
      else if (dirOnlyRadioButton.isSelected() == true)
         filesArrayList = Moose_Utils.getAllFilesInDirectory (folderPathTextField.getText() );

      System.out.println ("-> " + filesArrayList.size() + " files found.");


      // (1). Remove all NON SUPPORTED files from the filesArrayList.  Ref: SUPPORTED_INPUT_FILE_TYPES
      // Go in reverse to save an IF test.
      for (int k = filesArrayList.size() - 1; k >= 0; k--)
      {
         String fileNameStr      = filesArrayList.get(k).toString(); // File name with full absolute path.
         String fileExtensionStr = Moose_Utils.getFileExtensionFromStr (fileNameStr).toUpperCase();

         //System.out.println ("File: "   + fileNameStr);
         //System.out.println ("-> ext: '" + fileExtensionStr + "'");

         if ((fileExtensionStr.length() == 0) ||
             (SUPPORTED_INPUT_FILE_TYPES.indexOf (fileExtensionStr, 0) < 0) )
         {
            nonVideoFilesArrayList.add (filesArrayList.get(k) );

            filesArrayList.remove (k);
         }
      }

      System.out.println ("-> " + filesArrayList.size() +
                          " " + SUPPORTED_INPUT_FILE_TYPES + " supported video files found.");

      /*
      for (int k = 0; k < filesArrayList.size(); k++)
      {
         System.out.println (k + ". " + filesArrayList.get(k).toString() ); // File name with full absolute path.
      }
      */


      // (2). If an MP4, AVI, MOV file already has a corressponding "_ff.MP4" processed version,
      //      remove both from the filesArrayList.
/*
      for (int k = 0; k < filesArrayList.size(); k++)
      {
         String fileNameStr = filesArrayList.get(k).toString(); // File name with full absolute path.

         if (fileNameStr.endsWith ("_ff.mp4") == false)
         {
            // See if "_ff.mp4" of the MP4 file exists in the filesArrayList
            int index = -1;

            String ffMPeggedFileNameStr = Moose_Utils.addFileNamePrefixBeforeExtensionFromStr (fileNameStr, "_ff");
            ffMPeggedFileNameStr = Moose_Utils.setNewFileExtensionFromFile (ffMPeggedFileNameStr, "mp4");

            //System.out.println (" --> searching for '" + ffMPeggedFileNameStr + "' ...");

            for (int m = 0; m < filesArrayList.size(); m++)
            {
               //System.out.println ("--> Files match ? : '" + ffMPeggedFileNameStr +
               //    "'  VS  '" + filesArrayList.get(k).toString() +
               //    "': '"     + ffMPeggedFileNameStr.equalsIgnoreCase (filesArrayList.get(m).toString() ) );

               if ((m != k) &&
                   (ffMPeggedFileNameStr.equalsIgnoreCase (filesArrayList.get(m).toString() ) == true) ) // File name with full absolute path.
               {
                  index = m; // Found !
                  m     = filesArrayList.size(); // Exit loop
               }
            }
            //System.out.println (" --> index: " + index);

            if (index >= 0)
            {
               int minIndex = Math.min (index, k);
               int maxIndex = Math.max (index, k);

               // The MP4, AVI, MOV file and the "*_ff.mp4" exist ... remove both from the arraylist.
               // Must remove the maxIndex first ... because everything after this shuffles down.
               // If we removed minIndex first, then we would have to remove maxIndex - 1.
               //filesArrayList.remove (maxIndex);

               if (minIndex != maxIndex)
               {
                  //filesArrayList.remove (minIndex);
               }
            }
         }
      }

      System.out.println ("-> " + filesArrayList.size() + " Non-FFMpeg'd MP4, AVI, MOV files found (no corressponding '_ff.mp4').");
*/


      // (3).  Add "_ff.MP4" files to ArrayList.
      // (4).  Remove Zoom lecture recordings (they are already FFMpeg'd)
      //       Remove Video Camera files.
      //       Remove common folder files.
      // Go in reverse to save an IF test.

      ArrayList<String> fFMPegedMP4FilesArrayList = new ArrayList<String>();

      for (int k = filesArrayList.size() - 1; k >= 0; k--)
      {
         String fileNameStr      = filesArrayList.get(k).toString(); // File name with full absolute path.
         String fileExtensionStr = Moose_Utils.getFileExtensionFromStr (fileNameStr);

         // File.getName():  File name (no path).
         String fileNameNoPathStr  = filesArrayList.get(k).getName().trim(); // File name (no path).


         // v0.016: If a _ff.mp4, _ff.avi, etc of a file already exists, remove it from the list.
         boolean remove = false;
         for (int sup = 0; sup < SUPPORTED_INPUT_FILE_TYPES_ARRAY.length; sup++)
         {
            if (SUPPORTED_INPUT_FILE_TYPES_ARRAY[sup].length() > 0)
            {
               String fileEndStr = "_ff." + SUPPORTED_INPUT_FILE_TYPES_ARRAY[sup].toLowerCase();

               if (fileNameStr.toLowerCase().endsWith (fileEndStr)                 == true)
               {
                  remove = true;
               }
            }
         }



         if (remove  == true)
         {
            fFMPegedMP4FilesArrayList.add (fileNameStr);

            filesArrayList.remove (k);
         }

         else if ((fileNameStr.toLowerCase().contains ("00_common_files")    == true) ||
             (fileNameNoPathStr.toLowerCase().startsWith ("zoom_")           == true) ||
             (fileNameNoPathStr.toLowerCase().startsWith ("mah0")            == true) ||
             (fileNameNoPathStr.toLowerCase().startsWith ("sam_")            == true) ||
             (fileNameStr.toLowerCase().contains ("- mah0")                  == true) ||
             (fileNameStr.toLowerCase().contains ("- sam_")                  == true) ||
             (fileNameStr.toLowerCase().contains ("zoom_")                   == true) ||
             (fileNameStr.toLowerCase().contains ("zoom-meeting-invitation") == true) )
         {
            // Exclude files containing these strings ... Zoom, Digital Camera recordings, etc
            // are all the smalles files in my tests.
            // If they are re-processed with FFmpeg they are bigger files !!

            filesArrayList.remove (k);
         }
      }

/*
      // (3).  For each "_ff.MP4" file, remove the original MP4, AVI, MOV file -
      //       as these have already been processed.
      for (int ff = 0; ff < fFMPegedMP4FilesArrayList.size(); ff++)
      {
         // If the FFMPeged file is: abc_ff.mp4
         // then look for abc.mp4, abc,avi, abc.mov and remove all of these.
         String fileNameStr     = fFMPegedMP4FilesArrayList.get(ff);
         String mp4FileStr      = fileNameStr.replace ("_ff", "");
         String aviFileStr      = Moose_Utils.setNewFileExtensionFromFile (mp4FileStr, "avi");
         String movFileStr      = Moose_Utils.setNewFileExtensionFromFile (mp4FileStr, "mov");

         for (int k = filesArrayList.size() - 1; k >= 0; k--)
         {
            if ((filesArrayList.get(k).toString().compareToIgnoreCase (mp4FileStr) == 0) ||
                (filesArrayList.get(k).toString().compareToIgnoreCase (aviFileStr) == 0) ||
                (filesArrayList.get(k).toString().compareToIgnoreCase (movFileStr) == 0) )
            {
               filesArrayList.remove(k);
            }
         }
      }
*/

      for (int k = 0; k < filesArrayList.size(); k++)
      {
         //System.out.println (k + ". " + filesArrayList.get(k).toString() ); // File name with full absolute path.

         resultsTextArea.append (filesArrayList.get(k).getName() + "\n");
      }

      long totalFileSizeBytes = getSizeOfAllFilesInArrayList (filesArrayList);

      System.out.println ();
      System.out.println ("-> " + filesArrayList.size() +
                          " " + SUPPORTED_INPUT_FILE_TYPES + " files to be processed.");
      System.out.println ("-> Total size: " + Moose_Utils.scaleBytesToKBMBGBTBWithUnitsStr (totalFileSizeBytes, 1) );

      resultsTextArea.append ("-> " + filesArrayList.size() + " " +
                              SUPPORTED_INPUT_FILE_TYPES + "files listed." + "\n");

   }

   private long getSizeOfAllFilesInArrayList (ArrayList<File> filesArrayList)
   {
      long totalFileSizeBytes = 0;

      if (filesArrayList != null)
      {
         for (int k = 0; k < filesArrayList.size(); k++)
         {
            totalFileSizeBytes += Moose_Utils.getFileSizeBytes (filesArrayList.get(k) );
         }
      }
      return totalFileSizeBytes;
   }

   private void generateMP4FFBat ()
   {
      if ((filesArrayList == null) || (filesArrayList.size() == 0) )
      {
         JOptionPane.showMessageDialog
            (FFMpegFrontEnd.this, "ERROR: no files found.",
            "Error",  JOptionPane.ERROR_MESSAGE);

         return;
      }

      StringBuffer sb = new StringBuffer();
      int fileCount  = 0;
      int totalFiles = 0;

      resultsTextArea.setText("");

      sb.append ("echo off" + "\n");
      sb.append ("cls"      + "\n");

      // ffmpeg.exe -i a.mp4 a_ff.mp4
      for (int k = 0; k < filesArrayList.size(); k++)
      {
          File sourceFile = filesArrayList.get(k); // could be any of the supported video files.  Ref: SUPPORTED_INPUT_FILE_TYPES
          File destFile   = Moose_Utils.addFileNamePrefixBeforeExtensionFromFile (sourceFile, "_ff");

          // Make sure the destFile has an mp4 extension.
          destFile = Moose_Utils.setNewFileExtensionFromFile (destFile, "mp4");

          if (Moose_Utils.fileExists (destFile)              == false)
          {
             totalFiles++;
          }
      }


      // ffmpeg.exe -i a.mp4 a_ff.mp4
      for (int k = 0; k < filesArrayList.size(); k++)
      {
          File sourceFile = filesArrayList.get(k); // could be any of the supported video files.  Ref: SUPPORTED_INPUT_FILE_TYPES
          File destFile   = Moose_Utils.addFileNamePrefixBeforeExtensionFromFile (sourceFile, "_ff");

          // Make sure the destFile has an mp4 extension.
          destFile = Moose_Utils.setNewFileExtensionFromFile (destFile, "mp4");

          if (Moose_Utils.fileExists (destFile)              == false)
          {
             fileCount++;

             sb.append ("echo Processing File " + fileCount + " / " + totalFiles + ":" + "\n");

             sb.append ("ffmpeg.exe -i " +
                        "\"" + sourceFile.toString() + "\"" + "  " +
                        " -map_metadata -1 -map_chapters -1 " +   // Remove chapter markers, tags
                        "\"" + destFile.toString()   + "\"" + "\n" );

             resultsTextArea.append (sourceFile.getName()  + "\n");
          }
      }

      sb.append ("echo ."           + "\n");
      sb.append ("echo ."           + "\n");
      sb.append ("echo ----------------------------------------------------------------------"      + "\n");
      sb.append ("echo *** COMPLETED / DONE ! ***"      + "\n");
      sb.append ("echo *** COMPLETED / DONE ! ***"      + "\n");
      sb.append ("echo *** COMPLETED / DONE ! ***"      + "\n");
      sb.append ("echo *** COMPLETED / DONE ! ***"      + "\n");
      sb.append ("echo ----------------------------------------------------------------------"      + "\n");
      sb.append ("echo ."           + "\n");
      sb.append ("echo ."           + "\n");
      sb.append ("pause"            + "\n");

      resultsTextArea.append ("-> " + totalFiles +
                              " " + SUPPORTED_INPUT_FILE_TYPES + " files to be processed in '" +
                              "generate_MP4_ff_files.bat" + "'." + "\n");


      Moose_Utils.writeOrAppendStringToFile ("generate_MP4_ff_files.bat", sb.toString(), false);
   }

   private void generateDeleteRedundantMp4Bat ()
   {
      if ((filesArrayList == null) || (filesArrayList.size() == 0) )
      {
         JOptionPane.showMessageDialog
            (FFMpegFrontEnd.this, "ERROR: no files found.",
            "Error",  JOptionPane.ERROR_MESSAGE);

         return;
      }

      StringBuffer sb = new StringBuffer();

      sb.append ("echo off" + "\n");
      sb.append ("cls"      + "\n");

      for (int k = 0; k < filesArrayList.size(); k++)
      {
          File sourceFile = filesArrayList.get(k);
          File destFile   = Moose_Utils.addFileNamePrefixBeforeExtensionFromFile (filesArrayList.get(k), "_ff");

          // Make sure the destFile has an mp4 extension.
          destFile = Moose_Utils.setNewFileExtensionFromFile (destFile, "mp4");


          if (Moose_Utils.fileExists (destFile)              == true)
          {
             long sourceFileBytes = Moose_Utils.getFileSizeBytes (sourceFile);
             long destFileBytes   = Moose_Utils.getFileSizeBytes (destFile);

             System.out.println ();
             System.out.println (sourceFile.toString() + " --> " + sourceFileBytes + " bytes.");
             System.out.println (destFile.toString()   + " --> " + destFileBytes   + " bytes.");


             // If destFile has been shrunk but not TOO much and is in acceptable limits:
             // > 0.20 to get cater for errors, truncated files, etc
             // < 0.90 to discard "_ff.mp4" files that were not shrunk enough.
             if ((destFileBytes > (long) sourceFileBytes * 0.20) &&
                 (destFileBytes < (long) sourceFileBytes * 0.90) &&
                 (destFileBytes > 1000) )
             {
                //  The "_ff.mp4" exists, and is an acceptable size, so delete the original "mp4" file.
                sb.append ("del " + "\"" + sourceFile.toString() + "\"" + "\n" );
             }
             else
             {
                // The dest file is shrunk too much or not enough to be worth keeping instead of the original
                // so get rid of it.
                sb.append ("del " + "\"" + destFile.toString() + "\"" + "\n" );

                // TODO: rename the original file to "_orig_ff" so that we don't re-process this file again in the future.
                //
                File newSourceFile   = Moose_Utils.addFileNamePrefixBeforeExtensionFromFile (filesArrayList.get(k), "_orig_ff");
                Moose_Utils.renameFile (sourceFile, newSourceFile);
             }
          }
      }

      sb.append ("echo DONE !"      + "\n");
      sb.append ("pause"            + "\n");

      Moose_Utils.writeOrAppendStringToFile ("delete_redundant_files.bat", sb.toString(), false);
   }

   private void generateFFMp4FileSizeChangePercent ()
   {
      if ((filesArrayList == null) || (filesArrayList.size() == 0) )
      {
         JOptionPane.showMessageDialog
            (FFMpegFrontEnd.this, "ERROR: no files found.",
            "Error",  JOptionPane.ERROR_MESSAGE);

         return;
      }

      resultsTextArea.setText("");

      long totalBytesReduction  = 0;
      long totalSourceFileBytes = 0;
      long totalDestFileBytes   = 0;
      int  count = 0;


      for (int k = 0; k < filesArrayList.size(); k++)
      {
          File sourceFile = filesArrayList.get(k);
          File destFile   = Moose_Utils.addFileNamePrefixBeforeExtensionFromFile (filesArrayList.get(k), "_ff");

          // Make sure the destFile has an mp4 extension.
          destFile = Moose_Utils.setNewFileExtensionFromFile (destFile, "mp4");

          if (Moose_Utils.fileExists (destFile)              == true)
          {
             long sourceFileBytes = Moose_Utils.getFileSizeBytes (sourceFile);
             long destFileBytes   = Moose_Utils.getFileSizeBytes (destFile);
             long bytesReduction  = 0;

             System.out.println ();
             System.out.println (sourceFile.toString() + " --> " + sourceFileBytes + " bytes.");
             System.out.println (destFile.toString()   + " --> " + destFileBytes   + " bytes.");

             // Test data: new=55 old=60
             // fileChangePct = 100.0 * (55 - 60) / 60 = -8.3%
             double fileChangePct = 100.0 * (destFileBytes - sourceFileBytes)  / sourceFileBytes;

             // Exclude files that have negligible savings.
             //if ((destFileBytes > (long) sourceFileBytes * 0.20) &&
             //    (destFileBytes < (long) sourceFileBytes * 0.90) &&
             //    (destFileBytes > 1000) )
             //if (fileChangePct < -5.0) // Even simpler !
             //{
                bytesReduction       =  sourceFileBytes - destFileBytes;
                totalBytesReduction  += bytesReduction;

                totalSourceFileBytes += sourceFileBytes;
                totalDestFileBytes   += destFileBytes;
             //}

             count++;

             resultsTextArea.append (sourceFile.toString()  + "\n");
             resultsTextArea.append (" -> File " + (count) + " saved: " +
                                     Moose_Utils.scaleBytesToKBMBGBTBWithUnitsStr (bytesReduction, 1)
                                     + " (" + String.format ("%.1f", fileChangePct) + "%)" + "\n");
          }
      }

      double totalFileChangePct = 100.0 * (totalDestFileBytes - totalSourceFileBytes)  / totalSourceFileBytes;

      String summaryStr = "\n" + "*** Total disk space saved: " +
                          Moose_Utils.scaleBytesToKBMBGBTBWithUnitsStr (totalBytesReduction, 1)
                          + " (" + String.format ("%.1f", totalFileChangePct) + "%)" + "\n" + "\n";

      System.out.println     (summaryStr);
      resultsTextArea.append (summaryStr);

      Moose_Utils.writeOrAppendStringToFile ("file_change_pct_and_disk_space_reduction.txt", resultsTextArea.getText(), true);
   }


/*
   private void downloadUsingBAT ()
   {
      String cmdLineStr = DOWNLOAD_BAT_FILE;  // Run the BAT file.

      ArrayList<String> resultsArrayList = new ArrayList<String>();

      resultsTextArea.setText ("");

      // Add Date / Time and URL:
      resultsTextArea.setText ("\n\n" + folderPathTextField.getText () + "\n" +
                               Moose_Utils.getCurrentDateDDD_DDMMYYYY_HHMMSS_DATE_TIME_FORMAT() + "\n");

      // Write the date/time and URL out to the log so we have that
      // if the user cancels / kills the program.
      Moose_Utils.writeOrAppendStringToFile (LOG_FILE, resultsTextArea.getText() + "\n\n", true);

      //resultsArrayList.add ("NO Execute" + "\n");
      //resultsArrayList = WindowsCommandLine.executeConsoleCommandLine (cmdLineStr);
      resultsArrayList = WindowsCommandLine.executeShellCommandAndWait (cmdLineStr, "", true);

      for (int k = resultsArrayList.size() - 1; k > 0; k--)
      {
         String itemStr = resultsArrayList.get(k);
         itemStr = itemStr.trim();

         if (itemStr.length () == 0)
         {
            resultsArrayList.remove(k);
         }
         else
         {
            itemStr = "\t" + itemStr;
            resultsArrayList.set(k, itemStr);
         }
      }

      //resultsArrayList.add (0, Moose_Utils.getCurrentDateDDD_DDMMYYYY_HHMMSS_DATE_TIME_FORMAT());
      //resultsArrayList.add (1, folderPathTextField.getText ());

      Moose_Utils.CopyArrayListToJTextArea (resultsArrayList, resultsTextArea);

      resultsTextArea.setCaretPosition (0);  // Move cursor/view to start of data.

      //Moose_Utils.writeOrAppendStringToFile (LOG_FILE, resultsTextArea.getText() + "\n\n", true);
      Moose_Utils.writeOrAppendStringToFile (LOG_FILE, resultsTextArea.getText(), true);

   }
*/

   private void listNonVideoFiles ()
   {
      resultsTextArea.setText ("");

      if ((nonVideoFilesArrayList != null) && (nonVideoFilesArrayList.size() > 0) )
      {
         //ArrayList<File> list3ArrayList = Moose_Utils.getArrayListOfItemsInArrayList1AndNotInArrayList2 (allFilesArrayList, filesArrayList);

         Moose_Utils.copyArrayListToJTextArea (nonVideoFilesArrayList, resultsTextArea);

         resultsTextArea.append (" -> " + nonVideoFilesArrayList.size()  + " non-video files found." + "\n");
      }
   }


   public static void main (String args[])
   {
      FFMpegFrontEnd myApp = new FFMpegFrontEnd ();

      //myApp.setSize (950, 550);

      //myApp.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      myApp.setVisible (true);
   } // public static void main


} // public class FFMpegFrontEnd
