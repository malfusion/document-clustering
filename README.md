# Textual Data Clustering In Java
A Java experiment on clustering of textual data, dimensionality reduction and visualization

## Running the project
- Import the project into Eclipse and install maven dependencies. There are no external JAR dependencies or references.
- The program will output a topics.txt within the same folder for viewing
- The plot for Original vs (K-Means++ / Cosine Distance) Predictions are also present in the folder for reference.
- The output of the program is detailed and gives all the required details of accuracy, etc.
- Running the program with different Enum values for DistanceMeasure and CentroidInitializer will result in different results in the SVD and PCA visualizations.
- The program achieves the highest accuracies (sometimes 100%) with K-means++ and Cosine distance.