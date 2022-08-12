console.log('MangoTracko');
const mangoesElement = document.querySelector('.completed-mangoes');
const mangoChild = document.querySelector('.row.active-with-click');
const COMPLETION_DATE_LITERAL = "COMPLETION DATE: <br>";
var DateTime = luxon.DateTime;
// Get the header
var header = document.querySelector('.header');
// var navbar = document.querySelector('.nav-test');
// var header = document.getElementById(".header");
// Get the offset position of the navbar
// var sticky = header.offsetTop;

// const mango = {
//     mangoTitle: '',
//     img: '',
//     author: ''
// };

function initializeCompletedMangoPage()
{
    listCompletedMangoes();

}


function listCompletedMangoes() {
    let mangoTitle = '';
    let completionDate = 'COMPLETION DATE: ';
    let img = '';
    let author = '';

    axios.get('http://localhost:8080/completedMangoes')
        .then(response => {
            for(var i = 0;i < response.data.length; i++)
            {
                mangoTitle = response.data[i].mangoTitle;
                completionDate =  COMPLETION_DATE_LITERAL + DateTime.fromISO(response.data[i].completionDateTime).toFormat('EEE d LLL yyyy h:mm a');
                img = response.data[i].img;
                author = response.data[i].author;
                mangoChild.insertAdjacentHTML('beforeend', 
                `<div class="col-md-3">
                <article class="material-card Grey">
                    <div class="mc-content">
                        <div class="img-container">
                            <img class="img-responsive" src=${img}>
                        </div>
                        <h2 class="contentH2">
                        <span>${mangoTitle}</span>
                        <strong>
                            <i class="fa fa-fw fa-star"></i>
                            ${author}
                        </strong>
                    </h2>
                        <div class="mc-description">
                            <strong>${completionDate}</strong>
                        </div>
                    </div>
                    <a class="mc-btn-action">
                        <i class="fa fa-bars"></i>
                    </a>
                </article>
            </div>`);
            
            }
            $(function() {
                $('.material-card').materialCard({
                    icon_close: 'fa-chevron-left',
                    icon_open: 'fa-bars',
                    icon_spin: 'fa-spin-fast',
                    card_activator: 'click'
                });
            });
        })
        .catch(err => console.error(err.message));
}
window.addEventListener('scroll', function(){
    // var header = document.querySelector('.test');
    header.classList.toggle('sticky', window.scrollY > 0);
    // navbar.classList.toggle('sticky', window.scrollY > 0);
 });

 $(function(){
    $("#mango-header").load("header.html"); 
  });

  efo
//  $(function(){
//     $("#nav").load("navigation.html"); 
//   });



// When the user scrolls the page, execute myFunction
// window.onscroll = function() {myFunction()};

// Add the sticky class to the header when you reach its scroll position. Remove "sticky" when you leave the scroll position
// function myFunction() {
//   if (window.pageYOffset > sticky) {
//     header.classList.add("sticky");
//   } else {
//     header.classList.remove("sticky");
//   }
// } 