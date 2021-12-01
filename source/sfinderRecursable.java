import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sfinderRecursable extends PApplet {

BufferedReader fumen; // Create a reader to go through the contents of some text file that we will define later.
String fumenOutput; // Create a string that will eventually be used to store the output from the fumen reader described above.
String seed; // Create a string that will eventually be used to store the seed we're using.
PrintWriter file; // Marks the text file that everything is being put into.
int solutions; 
int start = 0;
int end = 0;
String seedString;
String[] allParams;
String params;
String seedReset;

public void setup() {
  fumen = createReader("output.html"); // Assign the reader to the file "output.html".
  file = createWriter("file.bat"); // Assign the writer to the file "file.bat".
  noLoop();
}

public void draw() {
      try { // "Try this on for size, mister."
      fumenOutput = fumen.readLine(); // Read the line of the output.html file we assigned earlier, and set it to the variable "fumenOutput".
      }
      catch (IOException e) { // If it doesn't work...
        file.flush(); // Stop writing things to the batch file.
        file.close(); // Close the batch file.
        exit(); // Exit the program.
      }
      allParams = loadStrings("input.txt");
      seed = allParams[0];
      params = allParams[1];
      seedReset = allParams[2];
      if (seedReset.equals("false")) {
        seedReset = "";
      } else {
        seedReset = ",*p7";
      }
      int startLine = fumenOutput.indexOf("<header>") + 8; // Attempt to grab the number in the phrase "# solutions" by finding the header tag.
      int endLine = fumenOutput.indexOf(" s"); // Attempt to find the end of the number in the phrase "# solutions" by searching for the space and first s.
      solutions = PApplet.parseInt(fumenOutput.substring(startLine, endLine)); // Set how many solutions there are based on the startLine and endLine.
      file.println("@echo off"); // Make it so the path directory doesn't show for every command in the batch file.
      file.println("if exist output rmdir /S output"); // Ask to delete the output directory if it already exists. Add /Q after /S if you don't want to be prompted.
      file.println("if not exist output mkdir output"); // Make output if it doesn't exist already.
      for (int i = 0; i < solutions; i++) { // Make a loop that will perform an action for every solution there is in the file.
      if (seed.equals("*p7")) { // If the seed file simply reads *p7 (the placeholder value)...
        seedString = "T,S,L,O,J,Z,I"; // Change it to a value that basically means the same thing but one that this program can interpret.
      } else {
        seedString = seed;
      }
      start = fumenOutput.indexOf("http://fumen.zui.jp/?v115", end) + 21; // Find the fumen by using the end of the last one and searching for the http link.
      end = fumenOutput.indexOf("' target", start); // Find the end of the fumen by searching for the closing apostrophe.
      int startPieces = fumenOutput.indexOf("blank'>", end) + 7;
      int endPieces = fumenOutput.indexOf("</a> [", startPieces);
      String usedPieces = fumenOutput.substring(startPieces, endPieces);
      usedPieces = usedPieces.replace("-Reverse",","); // Get rid of the extra unneeded fluff words
      usedPieces = usedPieces.replace("-Spawn",","); // Get rid of the extra unneeded fluff words
      usedPieces = usedPieces.replace("-Right",","); // Get rid of the extra unneeded fluff words
      usedPieces = usedPieces.replace("-Left",","); // Get rid of the extra unneeded fluff words
      usedPieces = usedPieces.replace(",", ""); // Get rid of commas for now
      usedPieces = usedPieces.replace(" ", ""); // Get rid of spaces for now
      println("The value of usedPieces is " + usedPieces + "."); // Debugging info to show which pieces were used.
      int numOfPieces = usedPieces.length(); // Get the number of characters in usedPieces (which will give how many pieces were used)
      for (int j = 0; j < numOfPieces; j++) { // For every piece that was used...
      seedString = seedString.replace("" + usedPieces.charAt(j) + "", ""); // Take the original string and check if that piece is in the original string, then replace it with a comma.
      }
      seedString = seedString.replace("," , "");
      seedString = seedString.replace(" ", ""); // Get rid of spaces for now
      println("Your current bag is " + seedString + ".");
      if (seedString == null) {
        seedString = "";
      }
      String output = fumenOutput.substring(start, end); // Make the fumen based on the two indexes we got earlier, start and end.
      
      // Below is where you're going to be changing everything.
      if (params.contains("percent")) {
      if (seedString.length() >= 2) { // Since both should be equally as likely in most cases.
        seedString = "[" + seedString + "]p" + seedString.length();
      }
      file.println("java -jar sfinder.jar " + params + " -P 2 --tetfu " + output + "vhAAAA -p \"" + seedString + "" + seedReset + "\" -lp output\\output-" + (i + 1) + ".txt | find \"success = \" > output\\tmp.txt"); // Put the command that runs sfinder.
      file.println("cd output && for /F \"tokens=3 delims= \" %%f in (tmp.txt) do (echo %%f percent solve chance && ren output-" + (i + 1) + ".txt %%f" + "-" + (i + 1) + ".txt && echo " + output + " >> %%f" + "-" + (i + 1) + ".txt)");
      file.println("del tmp.txt && if exist output-" + (i + 1) + ".txt del output-" + (i + 1) + ".txt");
      file.println("cd ..");  
    }
      if (params.contains("spin")) {
       file.println("java -jar sfinder.jar " + params + " -P 2 --tetfu " + output + "vhAAAA -p \"" + seedString + "" + seedReset + "\" -o output\\output-" + (i + 1) + ".html | find \"Found solutions = \" > output\\tmp.txt"); // Put the command that runs sfinder.
       file.println("cd output && for /F \"tokens=4 delims= \" %%f in (tmp.txt) do (echo %%f solutions && ren output-" + (i + 1) + ".html %%f" + "-" + (i + 1) + ".html && echo " + output + " >> %%f" + "-" + (i + 1) + ".html)");
       file.println("del tmp.txt && if exist output-" + (i + 1) + ".txt del output-" + (i + 1) + ".txt");
       file.println("cd ..");   
      }
      if (params.contains("path")) {
      if (seedString.length() >= 2) { // Since both should be equally as likely in most cases.
        seedString = "[" + seedString + "]p" + seedString.length();
      }
       file.println("java -jar sfinder.jar " + params + " -P 2 --tetfu " + output + "vhAAAA -p \"" + seedString + "" + seedReset + "\" -o output\\output-" + (i + 1) + ".html | find \"Found path [minimal] = \" > output\\tmp.txt"); // Put the command that runs sfinder.
       file.println("cd output && for /F \"tokens=5 delims= \" %%f in (tmp.txt) do (echo %%f minimals && ren output-" + (i + 1) + "_minimal.html %%f" + "-" + (i + 1) + ".html && echo " + output + " >> %%f" + "-" + (i + 1) + ".html && del output-" + (i + 1) + "_unique.html)");
       file.println("del tmp.txt && if exist output-" + (i + 1) + ".txt del output-" + (i + 1) + ".txt");
       file.println("cd ..");   
      }
      if (!params.contains("percent") && !params.contains("spin") && !params.contains("path")) {
        file.println("java -jar sfinder.jar " + params + " -P 2 --tetfu " + output + "vhAAAA -p \"" + seedString + "" + seedReset + "\" -o output\\output-" + (i + 1) + ".html");
      }
      }
        file.flush(); // Stop writing things to the batch file.
        file.close(); // Close the batch file.
        exit(); // Exit the program.
      }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sfinderRecursable" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
