@SetLocal EnableDelayedExpansion
	@For /D %%f In ("!ProgramFiles!\Java\*")      Do @Path %PATH%;%%~f\bin
	@For /D %%f In ("!ProgramFiles(x86)!\Java\*") Do @Path %PATH%;%%~f\bin
	@For    %%f In ("%~dp0sbt-launch-*.jar")      Do @Set "SNAP_JAR=%%~f"
	@Set MAX_MEMORY=512m
	@Start /D "%~dp0\.." java -Xmx"!MAX_MEMORY!" -XX:MaxInlineSize=100 -XX:FreqInlineSize=100 -XX:MaxPermSize=150m -XX:+UseConcMarkSweepGC -jar "!SNAP_JAR!" %*
@EndLocal
