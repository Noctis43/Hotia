<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    </div>
</main>

<footer class="site-footer">
    <div class="container footer-inner">
        <div class="footer-brand">
            <svg class="icon icon--sm" aria-hidden="true"><use href="#icon-mark"/></svg>
            <span>HoTia</span>
        </div>
        <p class="footer-copy">Rencontres authentiques, ancrées dans l'île — <span class="footer-year">2026</span></p>
    </div>
</footer>

<script>
(function () {
    var toggle = document.getElementById('navToggle');
    var nav = document.getElementById('navMain');
    if (toggle && nav) {
        toggle.addEventListener('click', function () {
            var open = nav.classList.toggle('is-open');
            toggle.setAttribute('aria-expanded', open ? 'true' : 'false');
        });
    }
})();
</script>
</body>
</html>
