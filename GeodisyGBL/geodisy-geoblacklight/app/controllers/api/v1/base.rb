module API
  module V1
    class Base < Grape::API
      mount API::V1::Stats
      mount API::V1::Info
      mount API::V1::Doc
    end
  end
end
