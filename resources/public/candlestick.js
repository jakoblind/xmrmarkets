//http://bl.ocks.org/abeppu/1074045
window.XMR = window.XMR || {};
(function(){
    "use strict";

    var width = 600;
    var height = 500;

    function min(a, b){ return a < b ? a : b ; }

    function max(a, b){ return a > b ? a : b; }

    function addLeadingZeros(input, max) {
        return ('0'+input).slice(-1 * max);
    }

    window.XMR.buildChart = function(data){
        var margin = 0;
        var marginLines = 50;

        d3.select("svg").remove();

        var chart = d3.select("#chart")
            .append("svg:svg")
            .attr("class", "chart")
            .attr("width", width)
            .attr("height", height);

        var y = d3.scale.linear()
            .domain([d3.min(data.map(function(x) {return x["low"];})), d3.max(data.map(function(x){return x["high"];}))])
            .range([height-marginLines, marginLines]);
        var x = d3.scale.linear()
            .domain([d3.min(data.map(function(d){return d.date})),
                     d3.max(data.map(function(d){return d.date}))])
            .range([marginLines,width-75]);

        chart.selectAll("line.x")
            .data(x.ticks(10))
            .enter().append("svg:line")
            .attr("class", "x")
            .attr("x1", x)
            .attr("x2", x)
            .attr("y1", marginLines)
            .attr("y2", height - marginLines)
            .attr("stroke", "#ccc");

        chart.selectAll("line.y")
            .data(y.ticks(10))
            .enter().append("svg:line")
            .attr("class", "y")
            .attr("x1", marginLines)
            .attr("x2", width - 75)
            .attr("y1", y)
            .attr("y2", y)
            .attr("stroke", "#ccc");

        chart.selectAll("text.xrule")
            .data(x.ticks(10))
            .enter().append("svg:text")
            .attr("class", "xrule")
            .attr("x", x)
            .attr("y", height - marginLines)
            .attr("dy", 15)
            .attr("text-anchor", "middle")
            .text(function(d){ var date = new Date(d * 1000); return addLeadingZeros(date.getHours(), 2) + ":" + addLeadingZeros(date.getMinutes(), 2); })
            .append('tspan')
            .text(function(d){ var date = new Date(d * 1000); return addLeadingZeros((date.getMonth() + 1), 2)+"/"+addLeadingZeros(date.getDate(),2); })
            .attr("x", x)
            .attr("y", height - marginLines);

        chart.selectAll("text.yrule")
            .data(y.ticks(10))
            .enter().append("svg:text")
            .attr("class", "yrule")
            .attr("x", width - marginLines)
            .attr("y", y)
            .attr("dy", 0)
            .attr("dx", 20)
            .attr("text-anchor", "middle")
            .text(function(a){return Math.round(a * 100000)/100000;});

        chart.selectAll("rect")
            .data(data)
            .enter().append("svg:rect")
            .attr("x", function(d) { return x(d.date)})
            .attr("y", function(d) {return y(max(d.open, d.close));})
            .attr("height", function(d) { return y(min(d.open, d.close))-y(max(d.open, d.close));})
            .attr("width", function(d) { return 0.5 * (width - 2*marginLines)/data.length; })
            .attr("fill",function(d) { return d.open > d.close ? "red" : "green" ;});

        chart.selectAll("line.stem")
            .data(data)
            .enter().append("svg:line")
            .attr("class", "stem")
            .attr("x1", function(d) { return x(d.date) + 0.25 * (width - 2 * marginLines)/ data.length;})
            .attr("x2", function(d) { return x(d.date) + 0.25 * (width - 2 * marginLines)/ data.length;})
            .attr("y1", function(d) { return y(d.high);})
            .attr("y2", function(d) { return y(d.low); })
            .attr("stroke", function(d){ return d.open > d.close ? "red" : "green"; })

    }

//    w.XMR.buildChart(pd);

})();
