(function () {
    'use strict';

    var card = document.getElementById('swipeCard');
    if (!card) {
        return;
    }

    var formGauche = document.querySelector('.swipe-form-gauche');
    var formDroite = document.querySelector('.swipe-form-droite');
    var reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    var delay = reducedMotion ? 0 : 280;

    function submitAfterExit(form, exitClass) {
        if (reducedMotion) {
            form.submit();
            return;
        }
        card.classList.add(exitClass);
        window.setTimeout(function () {
            form.submit();
        }, delay);
    }

    function swipeGauche() {
        if (formGauche) {
            submitAfterExit(formGauche, 'is-exit-left');
        }
    }

    function swipeDroite() {
        if (formDroite) {
            submitAfterExit(formDroite, 'is-exit-right');
        }
    }

    if (formGauche) {
        formGauche.addEventListener('submit', function (e) {
            e.preventDefault();
            swipeGauche();
        });
    }

    if (formDroite) {
        formDroite.addEventListener('submit', function (e) {
            e.preventDefault();
            swipeDroite();
        });
    }

    var startX = 0;
    var isDragging = false;

    card.addEventListener('mousedown', function (e) {
        if (e.target.closest('a')) {
            return;
        }
        startX = e.clientX;
        isDragging = true;
    });

    card.addEventListener('mouseup', function (e) {
        if (!isDragging) {
            return;
        }
        isDragging = false;
        var diff = e.clientX - startX;
        if (diff < -80) {
            swipeGauche();
        } else if (diff > 80) {
            swipeDroite();
        }
    });

    card.addEventListener('touchstart', function (e) {
        if (e.target.closest('a')) {
            return;
        }
        startX = e.touches[0].clientX;
    }, { passive: true });

    card.addEventListener('touchend', function (e) {
        var diff = e.changedTouches[0].clientX - startX;
        if (diff < -80) {
            swipeGauche();
        } else if (diff > 80) {
            swipeDroite();
        }
    }, { passive: true });
})();
