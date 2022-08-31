echo off
cls
echo Processing File 1 / 1:
ffmpeg.exe -i "C:\000 - TEMP\Sammi the Cocker Spaniel - 03 - dinner time - MAH00175.MP4"   -map_metadata -1 -map_chapters -1 "C:\000 - TEMP\Sammi the Cocker Spaniel - 03 - dinner time - MAH00175_ff.mp4"
echo DONE !
pause
