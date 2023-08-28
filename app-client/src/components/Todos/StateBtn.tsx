import { useState } from 'react';
import styled from 'styled-components';
import Button from '../UI/Button';

const Container = styled.div`
    margin-bottom: 30px;
`

const Wrapper = styled.div`
    justify-content:space-between;
`

const StateBtn = () => {
    const [showTodo, setShowTodo] = useState(true);

    const handleShowTodo = () => {
        setShowTodo(!showTodo);
    }

    return (
        <Container>
            <Wrapper>
                <Button showTodo={showTodo} onClick={handleShowTodo}>할 일</Button>
                <Button showTodo={!showTodo} onClick={handleShowTodo}>휴식</Button>
            </Wrapper>
        </Container>
    );
};

export default StateBtn;