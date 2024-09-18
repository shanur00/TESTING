import React, { useState } from 'react';
import axios from 'axios';

function SignIn() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const loginRequest = {
      username: username,
      password: password,
    };

    try {
      const response = await axios.post('http://localhost:8080/api/auth/signin', loginRequest);
      // Handle successful login response
      if(response.status === 200) {
        alert("Login successful");
        // Redirect or other actions
      }
    } catch (error) {
      if (error.response && error.response.data) {
        setMessage(error.response.data.message || 'Login failed. Please try again.');
      } else {
        setMessage('Login failed. Please try again.');
      }
    }
  };

  return (
    <div className="signin-container">
      <h2>Sign In</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Sign In</button>
      </form>
      {message && <p className="message">{message}</p>}
    </div>
  );
}

export default SignIn;
