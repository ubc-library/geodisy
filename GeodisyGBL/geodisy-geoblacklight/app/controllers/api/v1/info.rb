module API 
  module V1
    class Info < Grape::API
      include API::V1::Defaults
      resource :info do
        desc "Provide platform info"
	get "" do
	if headers['Accept'] == 'application/json'
	{
	"name": "Geodisy",
	"synopsis": "Geodisy is a research data discovery tool that harvests information from existing research data repositories and displays applicable records onto a map-based search interface.",
	"version": "1.0",
	"institution": "UBC",
	"releaseTime": "2020-03-20T13:00:00Z",
	"researchSubject": "Research data access and discovery",
	"supportEmail": "geodisy.info@ubc.ca",
	"tags": ["Metadata"]
	}
	else
	content_type "text/html"
        "<html>
	 <head>
         <title>Geodisy Info</title>
	 </head>
         <body>
         <p>Name: Geodisy</p>
         <p>Synopsis: Geodisy is a research data discovery tool that harvests information from existing research data repositories and displays applicable records onto a map-based search interface.</p>
         <p>Version: 1.0</p>
         <p>Institution: UBC</p>
         <p>Release Time: 2020-03-20T13:00:00Z</p>
         <p>Research Subject: Research data access and discovery</p>
         <p>Support Email: geodisy.info@ubc.ca</p>
         <p>Tags: Metadata</p>
         </body>
         </html>
	"
	end
        end
      end
    end
  end
end
