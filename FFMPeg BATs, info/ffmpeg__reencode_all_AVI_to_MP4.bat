REM FOR /R "C:\camtasia\" %%g IN (*.avi) DO (echo ffmpeg.exe -i "%%g" "%%~_ff.mp4"  ) )

REM for /R %%g in (*.avi) do ffmpeg.exe -threads 16 -i "%%g" "%%~_ff.mp4"
for /R %%g in (*.avi) do ffmpeg.exe -i "%%g" "%%~d_ff.mp4"


pause
