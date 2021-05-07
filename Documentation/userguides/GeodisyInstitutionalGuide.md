## Geodisy: Implementing a connection for data discovery at your institution

### What is Geodisy?
Geodisy is an open source discovery tool that allows users to find open data from Canadian researchers visually, spatially and quickly.
Research data can be hard to find, and even harder when looking for data about a specific area or place. Geodisy changes that, giving users a window into the world of research data with map-based tools familiar to everyone. Users can search by place name or using an interactive map. The Geodisy search interface will be available on Canada’s Federated Research Data Repository website (<https://geo.frdr-dfdr.ca>) and is accessible to the public. Data from institutions across Canada make up the source collection for Geodisy – making your data discoverable alongside other rich content deposited by researchers from across Canada.

#### How does Geodisy work?
Coordinates from both data and metadata are used to map each dataset by its location. Geodisy is not limited to traditional geospatial data - it can generate coordinates for any dataset associated with a terrestrial location. This makes it a powerful tool for many different subject areas.

Currently, Geodisy only works with open data (data sets that do not require authentication to download) in an instance of Dataverse 4.x. Unfortunately, Dataverse software versions prior to v4 are not compatible with Geodisy.

#### Use Geodisy to connect your institution’s data to FRDR
Connecting your Dataverse pipeline to Geodisy at FRDR involves very little work:

* To add your Dataverse to Geodisy’s pipeline, please email [geodisy.info@ubc.ca](mailto:geodisy.info@ubc.ca) and include the URL of the Dataverse.

And that’s it. If the Geodisy team discovers any issues or requires an API key for your Dataverse instance, they will contact you using the information you provide when uploading your data.

Once you’ve connected your institution's repository to Geodisy, your researchers may need to slightly alter their data deposit workflow in order to get the most out of Geodisy. To understand the minor changes that are required, please see [Geodisy: A guide for researchers depositing data in Dataverse](GeodisyDepositorGuide.md).

#### Use Geodisy to implement your own standalone data service
For institutions that do not wish to be part of FRDR’s Geodisy, it’s also possible to create a custom Geodisy instance at your own institution. This will require your institution to have the necessary infrastructure and components to run Geodisy and its discovery interface, GeoBlacklight.

All of the components involved in Geodisy are free and open source. More information can be found below:

* [Geodisy project on Github](https://github.com/ubc-library/geodisy)

Questions? Contact the Geodisy team at [geodisy.info@ubc.ca](mailto:geodisy.info@ubc.ca).
