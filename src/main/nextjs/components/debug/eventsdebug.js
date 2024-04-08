import {useState, useEffect} from "react";
import Draggable from "react-draggable";

export function DebugEvents(props) {

    var [showEvents, setShowEvents] = useState(false)
    const [events, setEvents] = useState()

    useEffect(() => {
        const timer = setInterval((cartItems) => {
            fetch(`http://localhost:8080/debug?aggregateId=${props.aggregateId}`).then(res => res.json()).then((events)=>setEvents(events))
        }, 2000);
        return () => clearInterval(timer);
    }, []);

    return <Draggable>
        <div className={"debug"}>

            <input type={"checkbox"} onChange={() => setShowEvents(!setShowEvents())}/><label
            className={"label"}>Debug</label>
            {showEvents ?
                <div>

                    <div>
                        {events?.map((item) => <div className={"notification is-info"}>
                            <div>{item.eventName}</div>
                            <div>
                                <pre id={"generatedScript"}>{JSON.stringify(item.event, null, 2)}</pre>
                            </div>
                        </div>)}
                    </div>
                </div> : <span/>}
        </div>
    </Draggable>
}
