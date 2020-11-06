module API 
  module V1
    class Stats < Grape::API
      include API::V1::Defaults
      resource :stats do
        desc "Provide platform stats" 

	# check service availability
	#if data.match(/DOWN/)
	#	status 503
	#end
	get "" do
	data = File.read("/tmp/wso2SiteMonitor.status")
	if data.match(/DOWN/)
                status 503
        else 
	if headers['Accept'] == 'application/json'
          content_type "application/json"
	  count = Searches.count
	  {
		"Searches": count,
		"lastReset": "2020-01-01T00:00:00Z",
	  }
	  #status 503
	else
	  count = Searches.count.to_s
	  content_type "text/html"
          "<html>
	   <head>
           <title>Geodisy Stats</title>
	   </head>
           <body>
           <p>Searches:" + count + "</p>
	   <p>lastReset:" + "2020-01-01T00:00:00Z" + "</p>
           </body>
           </html>
	  "
	end
        end
	end
      end
    end
  end
end
