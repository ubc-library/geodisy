module API 
  module V1
    class Doc < Grape::API
      include API::V1::Defaults
      resource :doc do
        desc "Provide platform doc"
	get "" do
		redirect "https://github.com/ubc-library/geodisy/blob/master/Documentation/documentationIndex.md", permanent: true 
		content_type "text/html"
        end
      end
      resource :releasenotes do
        desc "Provide platform release notes"
        get "" do
                redirect "https://github.com/ubc-library/geodisy/blob/master/Documentation/releaseNotes.md", permanent: true
                content_type "text/html"
        end
      end
      resource :support do
        desc "Provide platform support"
        get "" do
                redirect "https://github.com/ubc-library/geodisy/blob/master/Documentation/support.md", permanent: true
                content_type "text/html"
        end
      end
      resource :source do
        desc "Provide platform source code"
        get "" do
                redirect "https://github.com/ubc-library/geodisy", permanent: true
                content_type "text/html"
        end
      end
      resource :tryme do
        desc "Provide platform try me"
        get "" do
                redirect "https://geo.frdr.ca", permanent: true
                content_type "text/html"
        end
      end
      resource :licence do
        desc "Provide platform license"
        get "" do
                redirect "https://github.com/ubc-library/geodisy/blob/master/LICENSE", permanent: true
                content_type "text/html"
        end
      end
      resource :provenance do
        desc "Provide platform provenance"
        get "" do
                redirect "https://github.com/ubc-library/geodisy/blob/master/Documentation/provenance.md", permanent: true
                content_type "text/html"
        end
      end      
      resource :factsheet do
        desc "Provide platform fact sheet"
        get "" do
                redirect "https://github.com/ubc-library/geodisy/blob/master/Documentation/factsheet.md", permanent: true
                content_type "text/html"
        end
      end
    end
  end
end
