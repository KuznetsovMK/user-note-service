import './App.css';
import {NotFound} from "./pages/NotFound";
import {Header} from "./components/Header";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import {About} from "./pages/About";
import {Footer} from "./components/Footer";
import {NewNote} from "./pages/NewNote";
import {UpdateNote} from "./pages/UpdateNote";
import {Home} from "./pages/Home";

function App() {
    return (
        <Router basename='/my-notes'>
            <Header/>
            <main className="container content">
                <Switch>
                    <Route exact path='/' component={Home}/>
                    <Route path="/about" component={About}/>
                    <Route path='/note/find-one/:id' component={UpdateNote}/>
                    <Route path='/note/new-note' component={NewNote}/>
                    <Route component={NotFound}/>
                </Switch>
            </main>
            <Footer/>
        </Router>
    )
}

export default App;
