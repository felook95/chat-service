import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Button,
  Card,
  CardActions,
  CardContent,
  Container,
  TextField,
} from '@mui/material';
import { startConversation } from '../chat/api-conversation';

const Login = () => {
  const [values, setValues] = useState({ userName: '' });
  const navigate = useNavigate();

  const handleChange = (name) => (event) => {
    setValues({
      ...values,
      [name]: event.target.value,
    });
  };

  const handleSubmitClick = () => {
    startConversation().then(({ id }) => navigate(`/chat/${id}`));
  };

  return (
    <Container>
      <Card>
        <CardContent>
          <TextField
            id="username"
            type="text"
            label="Username"
            value={values.userName}
            onChange={handleChange('userName')}
          />
        </CardContent>
        <CardActions>
          <Button color="primary" onClick={handleSubmitClick}>
            Join
          </Button>
        </CardActions>
      </Card>
    </Container>
  );
};

export default Login;
