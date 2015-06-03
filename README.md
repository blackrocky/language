# Language Problem

You are given a number of files from a number of different human languages. All languages use the 
Latin alphabet A to Z; case can be ignored and the only punctuation characters are . , ; : and single 
space characters separating words. For example, you might have the files FRENCH.1, GAELIC.2 to 
GAELIC.9, ENGLISH.1 to ENGLISH.6 and so on. 

Given a file TEXT.txt in the same format but of unknown origin, Develop a program to identify its 
language.

# Assumptions
- Known language files have the language name for the filename and numbers as the extension eg. ENGLISH.1 ENGLISH.2 FRENCH.1 FRENCH.2 would have 2 languages: ENGLISH and FRENCH
- If characters other than allowed ones exist, program will fail
- If there is not enough data to determine the language, then UNKNOWN should be returned

# Software Needed
- JDK 7
- Maven 3.3.3

# Usage
- mvn clean compile test package
- cd target
- java -jar language-0.1.0.jar 

