REM FOR /R "C:\camtasia\" %%g IN (*.avi) DO (echo ffmpeg.exe -i "%%g" "%%~_ff.mp4"  ) )

REM for /R %%g in (*.avi) do ffmpeg.exe -threads 16 -i "%%g" "%%~_ff.mp4"
REM for /R %%g in (*.avi) do ffmpeg.exe -i "%%g" "%%~d_ff.mp4"

REM ffmpeg.exe -i "Sixth Sense, The.mp4" "Sixth Sense, The_ff.mp4"
REM ffmpeg.exe -i "Raging Bull.mp4" "Raging Bull_ff.mp4"
REM ffmpeg.exe -i "Battlefield Earth.m4v" "Battlefield Earth_ff.mp4"
REM ffmpeg.exe -i "Battleship.m4v" "Battleship_ff.mp4"
REM ffmpeg.exe -i "Master And Commander.mp4" "Master And Commander_ff.mp4"
REM ffmpeg.exe -i "Escape from LA (1996).mp4" "Escape from LA (1996)_ff.mp4"



ffmpeg.exe -i "Moon.m4v" "Moon_ff.mp4"
ffmpeg.exe -i "Lord of the Rings - 1 - Fellowship Of The Ring - Special Extended Edition (3h,38m).mp4" "Lord of the Rings - 1 - Fellowship Of The Ring - Special Extended Edition (3h,38m)_ff.mp4"

ffmpeg.exe -i "Never Mind The Buzzcocks - S..,E03 - Jack Dee With Katy Brand, Eliza Doolittle, Charlie Higson, Jedward.mp4" "Never Mind The Buzzcocks - S..,E03 - Jack Dee With Katy Brand, Eliza Doolittle, Charlie Higson, Jedward_ff.mp4"

ffmpeg.exe -i "Zombieland.m4v" "Zombieland_ff.mp4"

pause
