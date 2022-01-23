@echo off
if exist output/ rd output/
if exist file.bat del file.bat
if exist input.txt del input.txt
(echo *p7)>> "input.txt"
(echo percent -c 5)>> "input.txt"
(echo true)>> "input.txt"
(echo 5)>> "input.txt"
echo. >> "input.txt"
echo // First line should be the seed you used to get the spin.html file. Using *p7 will also work. >> "input.txt"
echo. >> "input.txt"
echo // Second line should be the parameters for the command you want, excluding --tetfu (-t), --patterns (-p), --page (-P), --log-path (-lp) and --output-base (-o). >> "input.txt"
echo. >> "input.txt"
echo // Set the third line to "true" if you want to regenerate a new bag. Set the third line to false if you only want exactly the pieces that are in the seed in the first line and nothing else. Should be true in nearly every case unless you're doing something like a quiz solve. >> "input.txt"
echo. >> "input.txt"
echo // Fourth line tells the program to only check options that have this number of pieces used for the solution or higher. It's mainly useful for the percent and path commands since they take a while if you search for too many extra pieces.