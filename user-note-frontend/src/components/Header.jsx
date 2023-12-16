function Header() {
    return <nav className="blue-grey darken-2">
        <div className="nav-wrapper">
            <a href="/" className="brand-logo">My NOTEs</a>
            <ul id="nav-mobile" className="right hide-on-med-and-down">
                <li><a href='/about'>About</a></li>
            </ul>
        </div>
    </nav>
}

export {Header}