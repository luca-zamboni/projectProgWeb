/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function(){
    var MAX_SIZE = 1024*1024;
    $("input[type='file']").change(function(){
        var file = $(this).get(0).files[0];
        console.log("buh!");
        if(typeof file != 'undefined' && file.size>0)
            if(file.size<MAX_SIZE)
                $("input[type='submit']").prop("disabled",false);
            else
                $("input[type='submit']").prop("disabled",true);
        else
            $("input[type='submit']").prop("disabled",true);
    });
});
