Taxi App

This repository contains the Taxi App project, developed as part of a programming test. The app includes functionalities related to ride management, organized in a modular design using Fragments and ViewModel.

Project Structure
The project is divided into:

MainActivity: Manages a ContainerView that displays the app's fragments.
Fragments: Each app screen is implemented in its respective fragment:
Registration/identification screen.
Ride history screen.
Map screen (with basic functionalities, but missing route tracing).
ViewModel: Centralizes variables and values shared between fragments.
Features
The app includes the following functionalities:

Customer Registration: Allows users to input a customer ID to fetch data from the backend.
Ride History: Displays a customer's ride history with filters by driver.
Map Display: Shows ride locations (partially implemented; route tracing is incomplete).
Note: The route tracing functionality on the map has not been implemented due to time constraints. This part of the code still needs development.

Technologies Used
Kotlin: Main programming language.
Retrofit: For integration with REST APIs.
Map library (MapView): Used to display the map interface in the app.
API Keys
This app does not use services that require API keys, such as Google Maps. The map functionality is based on open-source libraries like OSMDroid, which do not require authentication.

How to Run
Clone the repository:
bash
Copiar código
git clone https://github.com/JjuniorSilvaDev/iqwdf-wifoi.git
Open the project in Android Studio.
Connect a device or use an Android emulator.
Build and run the app.
Additional Information
GitHub Repository: https://github.com/JjuniorSilvaDev/iqwdf-wifoi
LinkedIn: https://www.linkedin.com/in/juniorsilva-dev/
This project demonstrates practical skills in Android development, though it is incomplete due to time limitations for implementing the route tracing functionality.