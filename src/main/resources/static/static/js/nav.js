// import * as ongoingMango from 'ongoingMango.js';
const ongoingMango = require('ongoingMango.js');
const completedMango = require('completedMango.js');

// import * as addMango from 'addMango.js';
// import * as finishMango from 'finishMango.js';


var navbar = document.querySelector('.nav-test');

document.getElementById("ongoingMango").addEventListener("click", ongoingMango.initializePage());
document.getElementById("completedMango").addEventListener("click", completedMango.initializePage());
// document.getElementById("addMango").addEventListener("click", ongoingMango.initializePage());
// document.getElementById("finishMango").addEventListener("click", ongoingMango.initializePage());



function myFunction() {
    var x = document.getElementById("myLinks");
    if (x.style.display === "none") {
        x.style.display = "block";
        // x.style.position = "fixed";
        // x.style.width = "100%";
        // x.style.zIndex = "14";
        // x.style.paddingBottom = "14px";
      } else {
        x.style.display = "none";
      }
   }

   window.addEventListener('scroll', function(){
    // var header = document.querySelector('.test');
   
    navbar.classList.toggle('sticky', window.scrollY > 0);
    
 });

 var prevScrollpos = window.pageYOffset;
 window.onscroll = function() {
   var currentScrollPos = window.pageYOffset;
   if (prevScrollpos > currentScrollPos) {
     document.getElementById("navbar").style.top = "0";
   } else {
     document.getElementById("navbar").style.top = "-50px";
   }
   prevScrollpos = currentScrollPos;
 }