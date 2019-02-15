import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
//import GroupList from './GroupList';
//import GroupEdit from './GroupEdit';
import SignUp from './SignUp.js';
import { CookiesProvider } from 'react-cookie';

class App extends Component {
    render() {
        return (
            <CookiesProvider>
            <Router>
                <Switch>
                    <Route path='/signup' exact={true} component={SignUp}/>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/hello' exact={true} component={Home}/>
                </Switch>
            </Router>
            </CookiesProvider>
        )
    }
}
export default App;
