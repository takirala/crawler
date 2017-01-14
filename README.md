## Requirements

java 8 has to be installed. That's all.

## How To Install

0 If you don't want to compile from source, skip to next section.
1 If you want to compile from the source, you need maven to install the packages and prepare the bundle.
2 You can use google to install maven. 
3 Once maven is installed, cd in to the home directory of where pom.xml is located and execute below commad:
	
		 mvn clean compile assembly:single
4 You should see a newly created directory (aptly) named as target. Continue to next section.

## How to Run

0 If you have followed previous section, you can cd in to the newly created target directory. Or you can extract the attached zip file and go in to the directory where **Crawler-0.1.0-jar-with-dependencies.jar** is located. 
1 Execute below command to run the code:
		
		 java -jar Crawler-0.1.0-jar-with-dependencies.jar
		 
2 If you don't want to compile from source, I have already attached the above jar file and you can execute the above command from where the jar is located.
3 The above command will keep printing some relevant meta data until all the crawling is over. 
4 The huge json dump will happen after all the urls have been crawled.

## Design considerations

* I have used jsoup to parse html and manipulate node attributes.
* I have used google's gson for pretty printing and parsing the data in json format.
* The code is very flexible and can accommodate many changes with very little change.
* The unit test cases do not make a lot of sense to have one for each method as the requirements are very specific.
* Some test cases have been added in Test.java

## How to run test cases.

* You can run tests using the command from the same directory as:

	java -cp Crawler-0.1.0-jar-with-dependencies.jar Test

* You can add more test cases in Test.java (You have to use maven to rebuild the bundle)

## Notes

* I have ran this code on gocardless.com and I observed that there are 3842 unique urls parsed. 
* Interestingly, there is only one http url under this domain. All other are https.
* I have attached the sample output as **output.json** for your reference.

I have very much enjoyed coding up this assignment. I have taken ~3.5 Hours to work on this. If taken more time, there would definitely be lot of areas for improvement (concurrent connections, optimal path wise parsing, multi threaded processing for result data set etc..). Although the code solves the very specific purpose, I have kept the modules fairly flexible. 

I would be happy to receive any sort of feedback on this. If you face any problems running the code or if you want to discuss the code, I am available at takirala@ufl.edu / tarugupta.92@gmail.com / skype:tarun.gupta.akirala