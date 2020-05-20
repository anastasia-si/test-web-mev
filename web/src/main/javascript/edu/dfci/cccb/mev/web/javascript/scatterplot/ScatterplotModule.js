(function(){

    define(['d3', 'angular', './lib/scatterplot'], function(d3, angular){

        var module = angular.module('Mev.ScatterPlotVisualization',[])

        module.directive('d3Scatterplot', [function(){

           return {

             scope:{
               data: "=",
               selected: "=",
               width: "=",
               height: "="
             },
             restrict: "E",
             link: function(scope, elements, attr){

                 var svg = d3.select(elements[0]).append("svg")
                     .attr('width', parseFloat(scope.width))
                     .attr('height', parseFloat(scope.height))

                 var scatterplot = d3.custom.scatterplot(svg)
                     .width(parseFloat(scope.width) - 100)
                     .height(parseFloat(scope.height) - 100)
                     .on('brushend', function(brush, points){
                         scope.selected = points
                         scope.$apply()
                     })

                 svg.call(scatterplot)
                 
                 initialCall = true

                 scope.$watchCollection('data', function(newval, oldData){
                     if ( (typeof newval != 'undefined') && initialCall){
                         scatterplot.dispatcher().draw(newval)
                         initialCall = false
                     } else if (newval) {
                         scatterplot.dispatcher().update(newval)

                     }
                 })

                 
             }

           } 

        }])

        return angular

    })

})()
