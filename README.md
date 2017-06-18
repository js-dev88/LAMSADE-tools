# LAMSADE-tools
[![Build Status](https://travis-ci.org/LAntoine/LAMSADE-tools.svg?branch=master)](https://travis-ci.org/LAntoine/LAMSADE-tools)

Le LAMSADE est une unité de recherche de Paris-Dauphine. Ce projet a pour but de fournir des outils de gestion administrative utiles à cette unité (ou plus généralement aux unités de recherche).

== Récits utilisateur
 - [x] Génération de papier à en-tête avec les coordonnées d’un chercheur passées en paramètres (voir link:LAMSADE[canevas]), format Asciidoc ou HTML ou OpenDocument Writer. **SetCoordinates**

 - [x] Génération de papier à en-tête d’après coordonnées indiquées dans l’interface graphique

 - [x] Récupération des coordonnées d’un chercheur depuis l’annuaire. **yearbookInfos**


 - [x] Génération du formulaire annuel de demande de PRES, d’après données passées en paramètres / fournies par interface graphique / récupérée depuis fichier / récupérée depuis internet

 - [x] Création conférence. URL, Titre, Date début et fin, Frais d’inscription. **Conferences**

 - [x] Génèration d'un fichier / entrée format VCal à partir des dates de conférences.

 - [ ] Lecture du fichier et édition conférences.

 - [x] Recherche d’une conférence existante : par nom, par URL… **Conferences**

 - [x] Utilisation format standard calendrier pour édition des conférences.

 - [ ] Moyen aller : a → b (bus) → c (avion) etc. Pour chaque transport, indiquer : fourchette prix, à rembourser ?, à acheter ?, original stocké ? \+ possibilité stocker fichier (ou donner URL ?). Moyen retour, même chose.

 - [x] Montrer périple sur une carte (lieu départ, lieu d’arrivée si rien d’autre rempli). **Map**

 - [ ] Sur place : l’utilisateur peut indiquer moyens de transports et fourchette prix.

 - [x] Génération d’une demande d’ordre de mission (OM) (voir link:LAMSADE/ordre_de_mission.ods[canevas] OM).

 - [x] Génération d’une demande de mission jeune chercheur (JC) (voir link:LAMSADE/demande_de_mission_jeune_chercheur.odt[canevas] JC).

 - [x] L’utilisateur peut entrer une demande scannée (après l’avoir signé à la main), le logiciel la conserve.

 - [x] Génération et stockage de la demande d’ordre de mission (OM) ou de la demande JC. Si déjà une, elle passe dans l’historique.

 - [x] L’utilisateur peut entrer son mot de passe de son compte e-mail @dauphine. Le logiciel le stocke en suivant le mécanisme de conservation des données secrètes de l’OS. - (not in GUI)

 - [ ] Réception des demandes scannées envoyées sur l’e-mail du chercheur (reconnaissable par l’adresse expéditeur : \photocopieur@lamsade.dauphine.fr), à la demande de l’utilisateur.

 - [X] Envoi demande par e-mail.

== Refs
* link:open_data.adoc[Dauphine Open Data] : récupération de données
* Cf. application link:Test-ODS[] (utilisant Apache ODFToolkit) pour modification de feuilles de calcul.

#####

Francisco Javier Martínez Lago  

What Javier has worked on:  
 - Created the initial .gitignore file.
 - Started work with Apache's PDFBox library to generate a PDF and created a function to add images to it.
 - Created function to determine the dimensions of an image
 - Created the Conference class as well as the initial main menu.
 - Set up Maven integration.
 - Configured Maven to use non-standard repos and to support different profiles in order to account for the user's operating system.
 - Created graphical interface and linked it to code to make a table showing all conferences.
 - Created a function to generate a calendar file and another to send it by email.
 - Set up Travis integration.
 - Created Class to handle showing itineraries in Google Maps from GUI.
 - Added a function to save a file to the project's directory and to handle cancellations and duplicates.
 - Fixed all errors caused by stricter Eclipse preferences file.
 - Implemented an external geocoding library to convert latitude and longitude to a city name
 - Implemented flight search functionality with city name validation
 
 What files has Javier contributed to :
https://gist.github.com/edoreld/430b72b342a7911f6e4820ad8292a71b


Julien has worked on:

- Refactoring of previous fonctional Classes made by Antoine and Abdel
- Package YearBook Info :
	-Class ConnectionToYearbook => JAXRS
	-Class GetInfosFromYearbook => JDOM then JSOUP and finally DOM
	-YearbookDataException
	-Unit Tests
- Set up of logging with log4j then slfj4j 
- GUI contact yearbook with name and firstname and fields to display informations
- Package MissionOrder :
	- Generate MissionOrder (User part)
	- History functions open / delete and getPath for GUI
- Keyring All the package's classes with other members
	- keyring for Windows Mac and Unix Windjpapi & jkeyring
	- fail to contact Outlook Exchange Serveur with my own adress from University with IMAP / IMAPS + Javaxmail
	- Table creation in database and functions
	- password encryption in database and session
- GUI Main Programm :
	- move buttons to the Mission Order part
	- Historic of MO (only GUI part)
	- delete a file from historic
	- Open a file in historic
	- Send an MO to an adress mail (only GUI) 
	- refactor Existing Itinerary Button to use Conference data
- Javadoc, test and import Maven librairies, small refactorings
  
 
 
AGBOIGBA Lionel has worked on :

- Creation of DOC’s version of “PapierAEntete” => Abandoned
	- Test with multiples libraries (docx4j, APACHE-POI, ...)
- Generation of ODT’s version of “PapierAEntete” with Apache ODF Toolkit 
- Use Jodconverter to convert ODT doc in a specific format (HTML, ASCIIDOC) => Abandoned
- The Database
	- Creation of new fields in the conferences’ database
	- Creation of new class to avoid to reapeted code
- Creation of template for PRES request and filled it 
- Creation of a function to historicize “Ordre de Mission” and “Demande jeune chercheur” docs 
- Creation of unit tests for classes in SetCoordinates package
- Javadoc and logging in SetCoordinates package
- Modifications of keyring's functions  
- Libraries' importation & testing in Maven


N.B. As a beginner in Java programming, I knew that I wouldn't be able to do as many things as my teammates for the project. It was really complicated for me to learn how Java works and to work for the project in the same time. Thanks to my teammates, I was able to do something decent.



Kévin has worked on :
- Started work with APACHE POI to set headed paper thanks to UserDetails class and created a clone of headed paper => Abandoned 
- Add functions to search a conference by title, url and location
- Add a function to clear Data Base if the table changes
- Create a new class for data base connexion to avoid to reapeted code for each function which call the database. 
- Add fields(city and address) in data base.
- Resolve somes problems of conference’s class ( because it does not worked because of severals updates in database) 
- Simplify the classe Conference in MVC(Model,View,Controller)
- Prevent SQL injection with PreparedStatement function
- Made tests for class’s in the conference package 
- Generate MissionOrder(conference part)
- Add user details to SWT window and modified User details
- Refactoring the field Address to Country 
- Complete OM (retreive Conference and users infos from GUI and set the informations into spreadsheet)


Antoine has worked on :
- The creation of the class GetInfosFromYearbook
- The creation of the class conference, the establishement of the database, the tests, and the division of the huge class it had become into several classes. The CLI is mostly from me.
- The creation of the class General menu which is a CLI which aside from giving access to all the fonctionality of the time also made work GetInfosFromYearBook and Setcoordinates together (obviously to do so I had to add and modify several methods in both classes)
- Solving issue #3 (Creation of .odt doc using a template). My solution uses regex, this is not very beautiful but it is  efficient and has been easy to copy in the fillPRES method createdby Lionel.
- Preventing SQL injections (the solution was found by Kevin, I only helped to implement it)
- As everybody, clean the code from comments and delete unused classes, notably in issue #49
- In the graphical interface:
	- added the button to general 'PapierEntete'
	- made the class Prefs to have the name and firstname kept from one launch to another
	- Launched the idea that it would better to be able to choose where the generated files go (implemented it with 	  the PreferencesWindow and modified GeneratePapierEntete to work with it) 
	- created the menu 'File', the preferences window it contains, and the menu 'Help' to have at least 2 menus


Abdelkader has worked on :

-GetInfosFromYearbook :
	¤ Creation of the class GetInfosFromYearbook => the first code of this class was reading through an html page code line by line.
	¤ Modification of the class GetInfosFromYearbook => DOM instead of JSOUP
	¤ Add behavior for letting the possibilty to use both login OR firstname + lastname
-UserDetails :
	¤ Modified the code so it can handle both login OR firstname + lastname
-Researched on how to use JAXRS
-Corrected the mistakes on the Maven integration
-Package map :
	¤ Creation of the classes AddressInfos, ConnectionToGoogleMapsAPI, ItineraryMap
	¤ Refactoring to use DOM
	¤ Unit Tests
-Graphical User Interface :
	¤ Group organization of the GUI (UserDetails group, Conference group ...)
	¤ Implementation of the button allowing to add a new conference into the database
	¤ First Itinerary button behavior, using ItineraryMap with OpenStreetMap (changed later for GoogleItineraryMap)
	¤ Generate a young searcher mission order
-missionOrder package :
	¤ Creation of the GenerateMissionOrderYS class using DOM
	¤ Creation of the History class
	¤ Worked on the historization
-Tried to implement the keyring, this was the most difficult task I had since there is not much documentation on that topic
-Javadoc, test, import Maven librairies, refactorings	

	

