/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    var toggleInviti = function(obj) {
        if ($(obj).prop('checked')) {
    console.log("asndjh");
            $("#inviti").slideDown();
        }
        else {
            $("#inviti").slideUp();
        }
    };
    $("#ckpriv").on('change', function() {
        toggleInviti($(this));
    console.log("asndjh");
    });
    toggleInviti($("#ckpriv"));
});
