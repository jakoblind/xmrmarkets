//http://bl.ocks.org/abeppu/1074045
window.XMR = window.XMR || {};
(function(){
    "use strict";

    var width = 600;
    var height = 500;
    var pd = [{"date":1409529600,"high":0.0046,"low":0.00428379,"open":0.00458841,"close":0.00433,"volume":43.696879377893,"quoteVolume":9940.45850734,"weightedAverage":0.0043958615536323},{"date":1409544000,"high":0.00475,"low":0.0043,"open":0.00433,"close":0.00461165,"volume":46.345531048904,"quoteVolume":10145.64269577,"weightedAverage":0.0045680231838074},{"date":1409558400,"high":0.00474988,"low":0.00458239,"open":0.00459382,"close":0.00468647,"volume":17.265557841615,"quoteVolume":3678.44082956,"weightedAverage":0.0046937163438567},{"date":1409572800,"high":0.00468648,"low":0.00432651,"open":0.00468647,"close":0.00435636,"volume":78.792116620383,"quoteVolume":17609.05247052,"weightedAverage":0.0044745233596352},{"date":1409587200,"high":0.00448998,"low":0.00408512,"open":0.00438659,"close":0.00434023,"volume":192.7023742401,"quoteVolume":45552.08535212,"weightedAverage":0.0042303743670679},{"date":1409601600,"high":0.00444988,"low":0.00428012,"open":0.00434023,"close":0.00433233,"volume":29.77706659522,"quoteVolume":6871.83947708,"weightedAverage":0.0043332017132439},{"date":1409616000,"high":0.004399,"low":0.004215,"open":0.00433232,"close":0.00425436,"volume":22.243546362246,"quoteVolume":5178.90970502,"weightedAverage":0.0042950249433168},{"date":1409630400,"high":0.00425436,"low":0.00414,"open":0.00425436,"close":0.00415,"volume":49.351083198237,"quoteVolume":11817.29100308,"weightedAverage":0.0041761756721887},{"date":1409644800,"high":0.00415976,"low":0.004,"open":0.00415,"close":0.00406724,"volume":72.090023148318,"quoteVolume":17688.49030969,"weightedAverage":0.0040755328400653},{"date":1409659200,"high":0.00419988,"low":0.00393736,"open":0.00406724,"close":0.00393736,"volume":57.784534798437,"quoteVolume":14345.05178086,"weightedAverage":0.0040281858637511},{"date":1409673600,"high":0.0042,"low":0.003942,"open":0.00397905,"close":0.00406071,"volume":70.845513274683,"quoteVolume":17430.76725657,"weightedAverage":0.0040643944257807},{"date":1409688000,"high":0.004199,"low":0.00405059,"open":0.00407989,"close":0.00411082,"volume":15.379099295065,"quoteVolume":3726.09752232,"weightedAverage":0.0041274011758795},{"date":1409702400,"high":0.00411081,"low":0.004,"open":0.00409124,"close":0.00406979,"volume":9.8523309378076,"quoteVolume":2446.33503395,"weightedAverage":0.0040273841485643},{"date":1409716800,"high":0.004093,"low":0.003963,"open":0.00404046,"close":0.004093,"volume":17.193345624726,"quoteVolume":4282.26293894,"weightedAverage":0.0040150139937419},{"date":1409731200,"high":0.00416,"low":0.00399001,"open":0.004093,"close":0.004,"volume":52.023377738371,"quoteVolume":12792.46166305,"weightedAverage":0.0040667214105192},{"date":1409745600,"high":0.00414985,"low":0.00399089,"open":0.00399089,"close":0.00407001,"volume":8.6305357361741,"quoteVolume":2114.25890076,"weightedAverage":0.0040820619144948},{"date":1409760000,"high":0.0041,"low":0.004,"open":0.00407001,"close":0.00408338,"volume":20.80970691548,"quoteVolume":5170.50440387,"weightedAverage":0.0040246957143881},{"date":1409774400,"high":0.00439307,"low":0.00408238,"open":0.00408338,"close":0.00425249,"volume":48.117539761029,"quoteVolume":11376.053293,"weightedAverage":0.0042297217252522},{"date":1409788800,"high":0.00449988,"low":0.00424,"open":0.00426048,"close":0.00445621,"volume":50.473868104904,"quoteVolume":11478.5366299,"weightedAverage":0.0043972389279507},{"date":1409803200,"high":0.00445621,"low":0.00433553,"open":0.00445621,"close":0.00434635,"volume":3.9720428492499,"quoteVolume":908.0573743,"weightedAverage":0.0043742201337353},{"date":1409817600,"high":0.00434635,"low":0.00434635,"open":0.00434635,"close":0.00434635,"volume":0,"quoteVolume":0,"weightedAverage":0},{"date":1409832000,"high":0.00434635,"low":0.00434635,"open":0.00434635,"close":0.00434635,"volume":0,"quoteVolume":0,"weightedAverage":0},{"date":1409846400,"high":0.00434635,"low":0.00373512,"open":0.00434635,"close":0.00392,"volume":97.674874337426,"quoteVolume":24632.79075592,"weightedAverage":0.0039652378532851},{"date":1409860800,"high":0.00458133,"low":0.00392,"open":0.00392,"close":0.00425,"volume":126.5628523661,"quoteVolume":29217.41208916,"weightedAverage":0.0043317612107425},{"date":1409875200,"high":0.00439988,"low":0.00419,"open":0.00425,"close":0.00437336,"volume":32.837732224155,"quoteVolume":7607.82420762,"weightedAverage":0.0043163105939363},{"date":1409889600,"high":0.00437336,"low":0.00423001,"open":0.00437336,"close":0.00423002,"volume":18.557767459415,"quoteVolume":4329.27284007,"weightedAverage":0.0042865784035721},{"date":1409904000,"high":0.00425762,"low":0.00419176,"open":0.00423035,"close":0.00419176,"volume":11.745778537924,"quoteVolume":2788.51908373,"weightedAverage":0.004212192273116},{"date":1409918400,"high":0.00425999,"low":0.00417157,"open":0.00419176,"close":0.00418403,"volume":19.565674116152,"quoteVolume":4667.40640376,"weightedAverage":0.0041919799613743},{"date":1409932800,"high":0.00419451,"low":0.00381012,"open":0.00417157,"close":0.0039932,"volume":115.18815368915,"quoteVolume":28885.05123472,"weightedAverage":0.0039878119915084},{"date":1409947200,"high":0.00414988,"low":0.00394988,"open":0.00395819,"close":0.00409983,"volume":59.054880050592,"quoteVolume":14744.77572241,"weightedAverage":0.0040051392549048},{"date":1409961600,"high":0.00406846,"low":0.00394988,"open":0.00406846,"close":0.00394988,"volume":13.916102304599,"quoteVolume":3485.78338063,"weightedAverage":0.0039922452961158},{"date":1409976000,"high":0.00404988,"low":0.003911,"open":0.00394,"close":0.00404988,"volume":18.616645821426,"quoteVolume":4655.47013167,"weightedAverage":0.0039988755796717},{"date":1409990400,"high":0.00411503,"low":0.00392016,"open":0.00402788,"close":0.00411,"volume":15.825822581494,"quoteVolume":3901.44388772,"weightedAverage":0.0040564014341732},{"date":1410004800,"high":0.00413571,"low":0.00401013,"open":0.00411,"close":0.004032,"volume":50.053084295411,"quoteVolume":12230.99523181,"weightedAverage":0.0040923149217845},{"date":1410019200,"high":0.0040843,"low":0.00396,"open":0.004032,"close":0.00403352,"volume":26.747913980678,"quoteVolume":6684.98301467,"weightedAverage":0.0040011940078203},{"date":1410033600,"high":0.00404988,"low":0.00392077,"open":0.00403351,"close":0.00402346,"volume":9.0712901712374,"quoteVolume":2267.23219968,"weightedAverage":0.0040010415221333},{"date":1410048000,"high":0.00408,"low":0.00396014,"open":0.00402346,"close":0.0040007,"volume":6.7087343973261,"quoteVolume":1664.36167823,"weightedAverage":0.00403081522789},{"date":1410062400,"high":0.0040178,"low":0.00396789,"open":0.00400057,"close":0.00396789,"volume":7.831615904941,"quoteVolume":1962.67466644,"weightedAverage":0.0039902771655714},{"date":1410076800,"high":0.00400338,"low":0.00390001,"open":0.00396789,"close":0.00390001,"volume":24.962919286507,"quoteVolume":6342.71623352,"weightedAverage":0.0039356828159177},{"date":1410091200,"high":0.0039619,"low":0.00381012,"open":0.00390001,"close":0.00390876,"volume":75.491554062931,"quoteVolume":19626.45812003,"weightedAverage":0.0038464176063376},{"date":1410105600,"high":0.003945,"low":0.00381,"open":0.00390876,"close":0.00394,"volume":34.591094498526,"quoteVolume":8918.63728829,"weightedAverage":0.0038785179148325},{"date":1410120000,"high":0.00394366,"low":0.00381012,"open":0.00394,"close":0.0039,"volume":21.508362494762,"quoteVolume":5548.87047914,"weightedAverage":0.003876169497129},{"date":1410134400,"high":0.003943,"low":0.0039,"open":0.0039,"close":0.0039,"volume":5.5379670820963,"quoteVolume":1414.03633804,"weightedAverage":0.003916424870504},{"date":1410148800,"high":0.00393394,"low":0.00382004,"open":0.00390001,"close":0.00382511,"volume":7.6661796987585,"quoteVolume":1980.6705825,"weightedAverage":0.0038704970763398},{"date":1410163200,"high":0.004,"low":0.00381315,"open":0.0038499,"close":0.00397354,"volume":32.927966744602,"quoteVolume":8386.60792132,"weightedAverage":0.0039262556510952},{"date":1410177600,"high":0.0040167,"low":0.00383,"open":0.00398351,"close":0.00384089,"volume":43.905749107693,"quoteVolume":11314.04795536,"weightedAverage":0.0038806401812088},{"date":1410192000,"high":0.00404988,"low":0.00383,"open":0.00386573,"close":0.00399362,"volume":48.709045294159,"quoteVolume":12362.80625512,"weightedAverage":0.0039399667267281},{"date":1410206400,"high":0.00402,"low":0.00385041,"open":0.00392808,"close":0.00395,"volume":70.906307579,"quoteVolume":17948.56909486,"weightedAverage":0.0039505270422535},{"date":1410220800,"high":0.00399,"low":0.00386,"open":0.00395,"close":0.00388,"volume":26.322574995839,"quoteVolume":6795.34177326,"weightedAverage":0.0038736204703375},{"date":1410235200,"high":0.00394,"low":0.00386,"open":0.00388,"close":0.0038696,"volume":10.511771134572,"quoteVolume":2713.12155079,"weightedAverage":0.0038744195340275},{"date":1410249600,"high":0.0039,"low":0.0038475,"open":0.0038696,"close":0.0038475,"volume":3.8297161123285,"quoteVolume":990.99315328,"weightedAverage":0.0038645232811679}]

    function min(a, b){ return a < b ? a : b ; }

    function max(a, b){ return a > b ? a : b; }

    window.XMR.buildChart = function(data){
        var margin = 50;

        var chart = d3.select("#chart")
            .append("svg:svg")
            .attr("class", "chart")
            .attr("width", width)
            .attr("height", height);
        var y = d3.scale.linear()
            .domain([d3.min(data.map(function(x) {return x["low"];})), d3.max(data.map(function(x){return x["high"];}))])
            .range([height-margin, margin]);
        var x = d3.scale.linear()
            .domain([d3.min(data.map(function(d){return d.date})),
                     d3.max(data.map(function(d){return d.date}))])
            .range([margin,width-margin]);

        chart.selectAll("line.x")
            .data(x.ticks(10))
            .enter().append("svg:line")
            .attr("class", "x")
            .attr("x1", x)
            .attr("x2", x)
            .attr("y1", margin)
            .attr("y2", height - margin)
            .attr("stroke", "#ccc");

        chart.selectAll("line.y")
            .data(y.ticks(10))
            .enter().append("svg:line")
            .attr("class", "y")
            .attr("x1", margin)
            .attr("x2", width - margin)
            .attr("y1", y)
            .attr("y2", y)
            .attr("stroke", "#ccc");

        chart.selectAll("text.xrule")
            .data(x.ticks(10))
            .enter().append("svg:text")
            .attr("class", "xrule")
            .attr("x", x)
            .attr("y", height - margin)
            .attr("dy", 20)
            .attr("text-anchor", "middle")
            .text(function(d){ var date = new Date(d * 1000);  return (date.getMonth() + 1)+"/"+date.getDate(); });

        chart.selectAll("text.yrule")
            .data(y.ticks(10))
            .enter().append("svg:text")
            .attr("class", "yrule")
            .attr("x", width - margin)
            .attr("y", y)
            .attr("dy", 0)
            .attr("dx", 20)
            .attr("text-anchor", "middle")
            .text(String);

        chart.selectAll("rect")
            .data(data)
            .enter().append("svg:rect")
            .attr("x", function(d) { return x(d.date)})
            .attr("y", function(d) {return y(max(d.open, d.close));})
            .attr("height", function(d) { return y(min(d.open, d.close))-y(max(d.open, d.close));})
            .attr("width", function(d) { return 0.5 * (width - 2*margin)/data.length; })
            .attr("fill",function(d) { return d.open > d.close ? "red" : "green" ;});

        chart.selectAll("line.stem")
            .data(data)
            .enter().append("svg:line")
            .attr("class", "stem")
            .attr("x1", function(d) { return x(d.date) + 0.25 * (width - 2 * margin)/ data.length;})
            .attr("x2", function(d) { return x(d.date) + 0.25 * (width - 2 * margin)/ data.length;})
            .attr("y1", function(d) { return y(d.high);})
            .attr("y2", function(d) { return y(d.low); })
            .attr("stroke", function(d){ return d.open > d.close ? "red" : "green"; })

    }

    window.XMR.buildChart(pd);

})();
