# Maze-Path-Prediction
This algorithm is used to predict the optimal path of a maze. The neural network trains on random mazes and their optimal lengths. The user then chooses a new maze to be input in a text file as provided in data1.txt. The maze in the text file is as such: it contains the maze in a line in terms of numbers (there are as many numbers as width x height (the width and height that the neural network has trained on)). An example would be as such:

Width = 4, Height = 4

User's maze:

X S O O

E X O O

O X O X

O O O X

S-Start, X-Wall, O-Path, E-End

In the text file, it should be as such:

3 1 2 2 4 3 2 2 2 3 2 3 2 2 2 3
