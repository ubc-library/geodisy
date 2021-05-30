## Geodisy : Mise en œuvre d’une connexion pour la découverte de données dans votre établissement


### En quoi consiste Geodisy ?
Geodisy est un outil de découverte à source ouverte qui permet aux utilisateurs de trouver les données ouvertes des chercheurs canadiens de manière visuelle, spatiale et rapide.
Les données de recherche peuvent être difficiles à trouver, surtout lorsqu’on cherche des données sur une région ou un endroit précis. Geodisy pallie cette difficulté en offrant aux utilisateurs une fenêtre sur le monde des données de recherche grâce à des outils cartographiques familiers. Les utilisateurs peuvent effectuer des recherches par nom de lieu ou à l’aide d’une carte interactive. L’interface de recherche Geodisy sera disponible sur le site web du Dépôt fédéré de données de recherche du Canada (https://geo.frdr-dfdr.ca/fr) et accessible au public. Les données provenant d’établissements de tout le Canada constituent la collection source de Geodisy, permettant la découverte de vos données ainsi que d’autres contenus riches déposés par des chercheurs du Canada.

### Comment fonctionne Geodisy ?
Le système de Geodisy utilise les coordonnées des données et des métadonnées pour cartographier chaque ensemble de données en fonction de son emplacement. Geodisy ne se limite pas aux données géospatiales traditionnelles ; il peut générer des coordonnées pour la plupart des ensembles de données associés à un emplacement terrestre. Ce qui fait de Geodisy un outil puissant pour divers domaines.

Actuellement, Geodisy ne fonctionne qu’avec des données ouvertes (ensembles de données ne nécessitant pas d’authentification pour être téléchargées) dans une instance de Dataverse 4.x. Malheureusement, les versions du logiciel Dataverse antérieures à la v4 ne sont pas compatibles avec Geodisy.

### Utiliser Geodisy pour connecter les données de votre établissement au DFDR
La connexion de votre pipeline Dataverse à Geodisy au DFDR nécessite très peu d’efforts :

* Pour ajouter votre Dataverse au pipeline de Geodisy, veuillez envoyer un courriel à [geodisy.info@ubc.ca](mailto:geodisy.info@ubc.ca) en y indiquant l’URL de votre Dataverse.

C’est aussi simple que ça. Si l’équipe Geodisy constate des problèmes ou a besoin d’une clé d’API pour votre instance Dataverse, elle vous contactera en utilisant les informations que vous aurez fournies lors du téléversement de vos données.

Une fois que vous avez connecté le dépôt de données de votre établissement à Geodisy, vos chercheurs devront peut-être modifier légèrement leur flux de travail pour le dépôt de leurs données afin de tirer le meilleur parti de Geodisy. Pour bien comprendre les changements mineurs qui sont nécessaires, veuillez consulter [Geodisy : Un guide à l’intention des chercheurs déposant des données dans Dataverse](https://github.com/ubc-library/geodisy/blob/master/Documentation/userguides/GeodisyDepositorsGuide-FR.md).

### Utiliser Geodisy pour mettre en œuvre votre service de données autonome
Si votre établissement ne souhaite pas faire partie de Geodisy du DFDR, il est également possible de créer une instance Geodisy personnalisée dans votre propre établissement. Dans ce cas, votre établissement devra disposer de l’infrastructure et des composants nécessaires pour faire fonctionner Geodisy et son interface de découverte, GeoBlacklight.

Tous les composants nécessaires à Geodisy sont gratuits et à source ouverte. Vous trouverez plus d’informations ci-dessous :

* [Projet Geodisy sur Github](https://github.com/ubc-library/geodisy)

Questions ? Communiquer avec l’équipe de Geodisy à [geodisy.info@ubc.ca](mailto:geodisy.info@ubc.ca).
 
