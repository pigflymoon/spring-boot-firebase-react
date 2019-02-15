import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { instanceOf } from 'prop-types';
import { Cookies, withCookies } from 'react-cookie';

class SignUp extends Component {
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    emptyItem = {
        name: '',
        email: '',
        password:'',
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            item: this.emptyItem,
            csrfToken: cookies.get('XSRF-TOKEN')
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }



    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
    item[name] = value;
        this.setState({item});
    console.log('item is :',item)
}

async handleSubmit(event) {
    event.preventDefault();
    const {item, csrfToken} = this.state;

await fetch('/api/signup', {
    method: (item.id) ? 'PUT' : 'POST',
    headers: {
        'X-XSRF-TOKEN': csrfToken,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(item),
    credentials: 'include'
});
    this.props.history.push('/');
}



render() {
    const {item} = this.state;
    const title = <h2>{item.id ? 'Sign Up' : 'Sign In'}</h2>;

    return <div>
        <AppNavbar/>
        <Container>
        {title}
            <Form onSubmit={this.handleSubmit}>
                <FormGroup>
                    <Label for="name">Name</Label>
                    <Input type="text" name="name" id="name" value={item.name}
                        onChange={this.handleChange} autoComplete="name"/>
                </FormGroup>
                <FormGroup>
                    <Label for="email">Email</Label>
                    <Input type="text" name="email" id="email" value={item.email}
                        onChange={this.handleChange} autoComplete="email-level1"/>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Password</Label>
                    <Input type="password" name="password" id="password" value={item.password}
                        onChange={this.handleChange} autoComplete="email-level1"/>
                </FormGroup>

                <FormGroup>
                    <Button color="primary" type="submit">Sign up</Button>{' '}
                    <Button color="secondary" tag={Link} to="/groups">Cancel</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>
}
}

export default withCookies(withRouter(SignUp));