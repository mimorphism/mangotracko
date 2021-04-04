console.log('MangoTracko');
const mangoesElement = document.querySelector('.mangoes');
const mangoChild = document.querySelector('.row.active-with-click');
const LAST_DATE_READ = "LAST READ: <br>";
const LAST_CHAPTER_READ = "LAST CHAPTER: <br>";
const API_URL = 'http://localhost:8081';
var DateTime = luxon.DateTime;
var header = document.querySelector('.header');

let loading = false;
let skip = 0;
let finished = false;



listAllMangoes();


function listAllMangoes() {
    let mangoTitle = '';
    let img = '';
    let author = '';
    let ongoingMangoInfo = '';


    axios.get(API_URL + '/ongoingMangoes')
        .then(response => {
            console.log(response);
            for(var i = 0;i < response.data.length; i++)
            {
                mangoTitle = response.data[i].mangoTitle;
                ongoingMangoInfo =  LAST_DATE_READ + DateTime.fromISO(response.data[i].ongoingMango.lastReadTime).toFormat('EEE LLL yyyy h:mm a')
                + '<br><br>' + LAST_CHAPTER_READ + response.data[i].ongoingMango.lastChapterRead;;
                img = response.data[i].img;
                author = response.data[i].author;
                localStorage.setItem(mangoTitle, JSON.stringify(response.data[i].ongoingMango));
                mangoChild.insertAdjacentHTML('beforeend', 
                `<div class="col-md-3">
                <article class="material-card Grey">
                    <div class="mc-content">
                        <div class="img-container">
                            <img class="img-responsive" src=${img}>
                        </div>
                        <h2 class="contentH2">
                        <a href="addMango.html?title=${mangoTitle}"><span>${mangoTitle}</span></a>
                        <strong>
                            <i class="fa fa-fw fa-star"></i>
                            ${author}
                        </strong>
                    </h2>
                        <div class="mc-description">
                            <strong>${ongoingMangoInfo}</strong>
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
                // window.setTimeout(function() {
                //     $('.material-card:eq(0)').materialCard('open');
                // }, 2000);
            
            });
        })
        .catch(err => console.error(err.message));
}
window.addEventListener('scroll', function(){
    // var header = document.querySelector('.test');
    header.classList.toggle('sticky', window.scrollY > 0);
 });
