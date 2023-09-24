document.addEventListener('DOMContentLoaded', function () {
    const viewLinks = document.querySelectorAll('.view a');
    const contentWrappers = document.querySelectorAll('.AllArtist__wrapper');

    function setActiveView(view) {
        contentWrappers.forEach(wrapper => {
            wrapper.style.display = 'none';
        });

        const targetWrapper = document.querySelector(`.${view}`);
        if (targetWrapper) {
            targetWrapper.style.display = 'grid';
        }

        localStorage.setItem('userViewChoice', view);
    }

    viewLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            const view = this.getAttribute('data-view');
            setActiveView(view);
        });
    });

    const savedView = localStorage.getItem('userViewChoice');

    if (savedView) {
        setActiveView(savedView);
    } else {
        const defaultView = viewLinks[0].getAttribute('data-view');
        setActiveView(defaultView);
    }

});


