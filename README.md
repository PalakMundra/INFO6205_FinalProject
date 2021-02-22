# INFO6205_FinalProject : Ranking EPL system

How to Run:
It is a Maven project based on JavaFX and Java 13+ (required for running the project)
The project can simply be launched by runnning the maven goal javafx:run or by going into the maven tab in IntelliJ and launching the javafx:run goal from Project->Plugins->javafx as long as the Project JDK has been set to Java 13 or above in the IntelliJ module settings

Please note that 2 of the UI screens take a bit of time to load where the team logos are included. That is a flaw in the way JavaFX is loading the images from the internet and not a problem with our code 


Project Description: The project aims at creating user interface predicting the ranking of the teams participating in the EPL.

• The project structure holds 2 main directories: Java and Resources
Resources

• Where the Java directory implements the object-oriented design concepts, definition of data structures, functionalities of the user interface and implementation of the Probability Density function; the Resource directory holds the datasets as well the XML structure definition of the user interface.

• We use Jackson library to convert our data into map structure which is then parsed for predictive analysis. We retrieve the team information participating in the EPL and the match results/fixtures through the ParseData class.


• The Context class, a singleton class is used as a bridge-storage class by the other classes where data retrieval and loading occurs.

• We call our main function in the App class which also initializes the Java FX for the Frontend of the UI.

• The Analyzer class hold the business logic of the entire program. Here the Probability Density function is built which is used for ranking teams in our project.

• Using the above mathematical analysis, design model and data parsing we have achieved:
1) Comparative analysis between two selected teams:
 This is used to calculate win, loss and draw probability between selected two teams.
 The output is represented as a probability distribution graph.
2) Generating current ranking of teams based on available data:
 Here we rank the teams as per current data taken from the external API which shows the total points scored by each team along with the number of matches won, lost and were draw.
 This table only considers the number of matches that a team has played.
3) Predictive ranking of teams in EPL 2019:
 Here our main objective is to predict the winning team for the year 2019.
 This table not only considers the matches that have been played earlier but also considers matches that need to be played and predict if a team is going to win or lose based on the probability distribution function.
 Here we also highlight the probability score for the prediction simulated by our code.
