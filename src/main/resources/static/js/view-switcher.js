document.addEventListener('DOMContentLoaded', function () {
    const viewLinks = document.querySelectorAll('.view a');
    const contentWrappers = document.querySelectorAll('.AllArtist__wrapper');

    function setActiveView(view) {
        console.log("Выбранный вид:", view);
        const baseView = view.replace(/-\d+$/, '');
        contentWrappers.forEach(wrapper => {
            wrapper.style.display = 'none';
        });

        const targetWrapper = document.querySelector(`.${baseView}`);
        if (targetWrapper) {
            targetWrapper.style.display = 'grid';
        } else {
            console.log("Обертка для выбранного вида не найдена!");
        }

        localStorage.setItem('userViewChoice', view);

        const viewIcons = document.querySelectorAll('.view-icon');
        viewIcons.forEach(icon => {
            const viewType = icon.parentElement.getAttribute('data-view');

            const isActive = view === viewType;

            const viewTypeNumber = viewType.substring(viewType.lastIndexOf('-') + 1);
            const newSrc = isActive ? `/images/artists-view/view-active-${viewTypeNumber}.svg` : `/images/artists-view/view-noactive-${viewTypeNumber}.svg`;


            icon.setAttribute('src', newSrc);
        });
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


