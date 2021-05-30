## Foire aux questions

### Comment puis-je faire une recherche avec carte dans Geodisy?

1. Déplacer la carte pour afficher votre zone d’intérêt
	* Utilisez les boutons + et - en haut à gauche de la carte pour agrandir et réduire la carte.
	* Utilisez la molette de votre souris pour agrandir et réduire la carte.
	* Cliquez et glissez sur la carte.
	* Maintenez la touche Majuscule enfoncée et cliquez pour dessiner une boîte pour l’agrandissement de la carte.
	* Sur un appareil mobile, utilisez deux doigts pour agrandir et glisser la carte.  

2. Cliquez **Chercher ici** pour voir les résultats	 

La «pertinence» des résultats de recherche est déterminée par la taille du cadre englobant. Les données dont les cadres englobants correspondent le mieux à la zone dans laquelle vous recherchez seront renforcées, tandis que les cadres englobants beaucoup plus grands ou plus petits que la zone représentée seront dissimulés.

### Que signifie le type de données  _undetermined_  lorsque je vois des résultats dans Geodisy?

_Geodisy_ moissonne toutes sortes de données; tous ces types de données ne sont pas explicitement géospatiaux. Les données  _Undetermined_ peuvent avoir plusieurs significations:

* Les données en question sont liées à un lieu, mais ne constituent pas un fichier géospatial traditionnel. En voici quelques exemples :

	* Des données statistiques sur un lieu, telles que celles d’un sondage auprès des résidents de Vancouver.  
	* Des données météorologiques telles que celles d’un fichier de valeurs séparées par des virgules d’informations météorologiques pour une province. Bien que les données concernent un lieu, il s’agit d’un fichier CSV sans coordonnées géospatiales, par exemple.
	
* L’ensemble de données _est_ géospatial, mais les systèmes de détection de Geodisy n’ont pas été capables de les classer par type. Les données géospatiales prennent une grande variété de formats qui ne sont pas tous communs ou détectables par tous les logiciels. En voici quelques exemples :  

	* Des données dans des formats plus anciens, tels que les formats d’échange Esri .e00
	* Des types de données spatiales peu courants, tels que les bases de données Spatialite  

Cela ne signifie pas que les ensembles de données ne fonctionneront pas avec un système d’information géographique, mais seulement qu’ils ne sont pas téléchargeables sous forme de fichiers Shapefile ou de données matricielles à partir de l’interface Geodisy. Ils sont toujours téléchargeables à partir du dépôt source en suivant le lien **Plus de détails au** link

### Quel type de données est recherché dans Geodisy?

Bien que _Geodisy_ soit un outil de recherche géospatiale, il recherche tout type de données ayant une composante géographique dans la _description_ des données. Ainsi, il est possible de trouver des données **à propos** d’un lieu. Traditionnellement, une recherche cartographique ne rapporte que les éléments qui peuvent être visualisés dans une application conçue pour la cartographie, mais Geodisy rapporte tous les types de données. La recherche de données au Nigeria rapportera des données d’enquête **à propos** du Nigeria, même si l’ensemble de données ne contient pas de points de latitude/longitude ou d’images.

### Est-ce que Geodisy contient des données restreintes?

Geodisy ne fonctionne qu’avec des ensembles de données en libre accès. Les ensembles de données comportant des restrictions de quelque nature que ce soit ne sont pas inclus. Puisque Dataverse ne fonctionne pas avec les données sous embargo, les ensembles de données soumis à un embargo ne sont pas non plus inclus.

### En quoi consiste le lien de métadonnées ISO 19139?

Tous les objets apparaissant dans _Geodisy_ se verront attribuer des métadonnées géospatiales[ISO 19115](https://www.iso.org/fr/standard/53798.html) geospatial metadata créées lors de leur ajout à Geodisy qui sont disponibles [ISO 19139](https://www.iso.org/fr/standard/32557.html) en format XML. La norme internationale ISO 19115 sert à décrire les données géographiques. Ce processus fait en sorte que:

* Toutes les métadonnées apparaissant dans _Geodisy_ auront un schéma cohérent et standardisé  
* Les informations géographiques absentes de la fiche source mais découvertes par _Geodisy_ seront ajoutées aux métadonnées de manière cohérente  
* Les métadonnées seront lisibles et utilisables par les systèmes d’information géographique actuels  

### Où puis-je trouver des liens vers des guides sur Geodisy et son fonctionnement?

La documentation sur Geodisy se trouve sur la page d’accueil: <https://github.com/ubc-library/geodisy/blob/master/Documentation/>

On y trouve des guides d’utilisation abrégés pour:

* L’ajout de métadonnées aux enregistrements afin qu’ils soient facilement repérables par Geodisy: [Guide à l’intention des déposants](https://github.com/ubc-library/geodisy/blob/master/Documentation/userguides/GeodisyDepositorGuide-FR.md)  
* La liaison de votre dépôt Dataverse à Geodisy au [Dépôt Fédéré de Données de Recherche](https://geo.frdr-dfdr.ca/fr): [Guide à l’intention des établissements](https://github.com/ubc-library/geodisy/blob/master/Documentation/userguides/GeodisyInstitutionalGuide-FR.md)

### Comment se fait la conversion d’enregistrements Dataverse en enregistrements Geodisy?

Les chercheurs n’ont qu’à déposer leurs données dans un dépôt [Dataverse](https://dataverse.org) avec une connexion à une instance de Geodisy. Si le dépôt contient les informations appropriées dans l’enregistrement _ou_ dans les fichiers connexes:

* L’enregistrement sera moissonné par _Geodisy_  
* Toutes les informations géographiques trouvées dans l’enregistrement d’étude et les fichiers connexes seront automatiquement moissonnées  
* Un enregistrement unique pour chaque fichier contenant des données sera créé dans _Geodisy_, et une zone géographique sera créée pour l’enregistrement en fonction de sa description ou du contenu du fichier
* Dans le cas de fichiers multiples contenant des données, plusieurs enregistrements _Geodisy_ seront créés pour chaque élément de données approprié et les fichiers connexes seront affichés
* Des métadonnées standardisées ISO 19115 seront créées  
* Des liens au dépôt source seront créés  
* Des cadres englobants pour les données moissonnées seront générés et trouvables dans l’interface  
* Des liens de téléchargement seront générés pour les types de fichiers valides  

		


 
