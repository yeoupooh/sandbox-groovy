def html = new URL("http://www.melon.com/chart/")
        .getText(connectTimeout: 5000,
        readTimeout: 10000,
        useCaches: true,
        allowUserInteraction: false,
        requestProperties: ['Connection': 'close'])

println html
