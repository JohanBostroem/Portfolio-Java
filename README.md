JPaint - A Simple Drawing Application
JPaint is a lightweight Java-based drawing application that allows users to create, manipulate, save, 
and load basic geometric shapes like circles and rectangles. 
The application is designed to be a simple yet effective tool for learning object-oriented programming and graphical user interfaces in Java.

Features
Drawing Shapes: Create and manipulate circles and rectangles on a canvas.
Save & Load: Save your drawings to a file and reload them later, maintaining their shapes, sizes, and colors.
Custom Colors: Define the color of shapes using hexadecimal color codes.
Intuitive Interface: Easy-to-use interface designed for experimenting with shapes.

How to Run

Prerequisites
Java Development Kit (JDK): Version 8 or higher.
IDE: (Optional) Any Java-compatible IDE such as IntelliJ IDEA, Eclipse, or VS Code.

Instructions

Clone the repository:
bash
Copy code
git clone https://github.com/your-username/jpaint.git
cd jpaint

Compile the project:
bash
Copy code
javac -d bin src/se/miun/id2207/dt187g/jpaint/*.java
Run the application:

bash
Copy code
java -cp bin se.miun.id2207.dt187g.jpaint.Main
Project Structure
bash
Copy code

src/
│
├── se/miun/id2207/dt187g/jpaint/
│   ├── geometry/           # Classes for geometric shapes (Circle, Rectangle, etc.)
│   ├── io/                 # File handling utilities for saving and loading drawings
│   ├── ui/                 # User interface components
│   └── Main.java           # Entry point of the application


Usage

Drawing Shapes:
Select the desired shape from the toolbar.
Click and drag on the canvas to draw the shape.

Saving a Drawing:
Click the "Save" button.
Enter a filename to save the drawing.
Loading a Drawing:

Click the "Load" button.
Select a previously saved file to load the drawing.
Example Screenshots
Include a few screenshots of the application interface and a sample drawing.


License
This project is licensed under the MIT License.

Author
Johan Boström

Acknowledgments
Course: Java II
Mid Sweden University

Libraries Used: AWT, Graphics2D, 
