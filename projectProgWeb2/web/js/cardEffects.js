/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    var loadCard = function() {
        $(".card ").each(function(index) {
            $(this).toggle("drop", {direction: "down"},1+index*70);
        });
    };
    setTimeout(loadCard, 500);
});
