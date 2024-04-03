
const lightThemeButton = document.querySelector('.theme_sun');
const darkThemeButton = document.querySelector('.theme_moon');
const classicThemeButton = document.querySelector('.theme_classic');
function setTheme(theme) {
    if (theme === 'light') {
        document.documentElement.style.setProperty('--black', '#F8F8F8');
        document.documentElement.style.setProperty('--white', '#0B0B0B');
        document.documentElement.style.setProperty('--black-layer', '#e6e6e6');
        document.documentElement.style.setProperty('--gray', '#0e0e0e');
        document.querySelector('.header__menu-top').style.background = '#1b1b1b';
        document.querySelectorAll('.icon-close').forEach(icon => {
            icon.style.filter = 'invert(1)';
        });
        document.querySelectorAll('.interesting-artist-link').forEach(artistLink => {
            artistLink.style.filter = 'invert(1)';
        });
        document.querySelectorAll('.view a').forEach(artistView => {
            artistView.style.filter = 'invert(1)';
        });
        document.querySelector('.controls').style.filter = 'invert(1)';
        document.querySelector('.audio-action_btn').style.filter = 'invert(1)';
    } else if (theme === 'dark' || theme === 'classic') {
        document.documentElement.style.setProperty('--black', '#0B0B0B');
        document.documentElement.style.setProperty('--white', '#F8F8F8');
        document.documentElement.style.setProperty('--black-layer', '#1A1A1A');
        document.documentElement.style.setProperty('--gray', '#CDCDCD');
        document.querySelector('.header__menu-top').style.background = 'transparent';
        document.querySelectorAll('.icon-close').forEach(icon => {
            icon.style.filter = 'invert(0)';
        });
        document.querySelectorAll('.interesting-artist-link').forEach(artistLink => {
            artistLink.style.filter = 'invert(0)';
        });
        document.querySelectorAll('.view a').forEach(artistView => {
            artistView.style.filter = 'invert(0)';
        });
        document.querySelector('.controls').style.filter = 'invert(0)';
        document.querySelector('.audio-action_btn').style.filter = 'invert(0)';
    }
}

lightThemeButton.addEventListener('click', (event) => {
    event.stopPropagation();
    setTheme('light');
    localStorage.setItem('theme', 'light');
});
darkThemeButton.addEventListener('click', (event) => {
    event.stopPropagation();
    setTheme('dark');
    localStorage.setItem('theme', 'dark');
})

classicThemeButton.addEventListener('click', (event) => {
    event.stopPropagation();
    setTheme('classic');
    localStorage.setItem('theme', 'classic');
});

document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        setTheme(savedTheme);
    }
});
