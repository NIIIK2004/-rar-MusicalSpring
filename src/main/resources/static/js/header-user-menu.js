document.addEventListener('DOMContentLoaded', function () {
    const headerUser = document.querySelector('.header_user');
    const headerUserMenu = document.querySelector('.header_user-menu');

    headerUser.addEventListener('click', function (event) {
        event.preventDefault();
        if (headerUserMenu.classList.contains('header_user-menu-active')) {
            headerUserMenu.classList.remove('header_user-menu-active');
        } else {
            headerUserMenu.classList.add('header_user-menu-active');
        }
    });

    document.addEventListener('click', function (event) {
        if (!event.target.closest('.header_user') && !event.target.closest('header_user-menu')) {
            headerUserMenu.classList.remove('header_user-menu-active');
        }
    });
});
