#
# = uri/https.rb
#
# Author:: Akira Yamada <akira@ruby-lang.org>
# License:: You can redistribute it and/or modify it under the same term as Ruby.
# Revision:: $Id: https.rb,v 1.4 2005/08/24 05:08:00 ryan Exp $
#

require 'uri/http'

module URI

  # The default port for HTTPS URIs is 443, and the scheme is 'https:' rather
  # than 'http:'. Other than that, HTTPS URIs are identical to HTTP URIs;
  # see URI::HTTP.
  class HTTPS < HTTP
    DEFAULT_PORT = 443
  end
  @@schemes['HTTPS'] = HTTPS
end
