$db = SQLite3::Database.open "/var/www/geoblacklight/geodisy/db/#{Rails.env}.sqlite3"

class Searches < ApplicationRecord 

#  attr_reader :id, :user_id, :user_type, :created_at, :updated_at

#  def self.search_count
#  	sorted = $db.execute("select count(*) from searches").flatten
#  end

end
