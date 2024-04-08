import Head from 'next/head';
import {useEffect, useRef, useState} from "react";
import {useCookie} from "react-use";
import {DebugEvents} from "../components/debug/eventsdebug";

function objectToRequestParams(obj) {
    return Object.entries(obj)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&');
}

const products = [
    {
        productName: "Filterkaffee",
        price: 7.99,
        productId: "4961b36a-f349-4b29-98d6-70e163a878c5",
        quantity: 1,
        productimage: "/assets/images/1.jpg"

    },
    {
        productName: "Espresso",
        price: 9.99,
        productId: "4b057fd2-13e1-4144-940a-462f842aa313",
        quantity: 2,
        productimage: "/assets/images/2.jpg"

    },
    {
        productName: "Lungo",
        price: 4.99,
        productId: "85e74ed6-3824-497d-b2c3-5359038928b5",
        quantity: 1,
        productimage: "/assets/images/3.jpg"

    }
]

export default function Home(props) {

    const [cartItems, setCartItems] = useState([])
    const [cartId, setCartId] = useState("c49a2449-c466-40f1-887f-4fee151966e0")
    const [inventories, setInventories] = useState([])

    function addCartItem() {
        const randomIndex = Math.floor(Math.random() * products.length);
        var product = products[randomIndex];

        fetch(`http://localhost:8080/additem?aggregateId=${cartId}&${objectToRequestParams(product)}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
        })
            .then(response => {
                fetchInventories(product.productId).then(fetchCart)
            })
    }

    function deleteCartItem(item) {
        const randomIndex = Math.floor(Math.random() * products.length);
        var product = products[randomIndex];

        fetch(`http://localhost:8080/removeitem?aggregateId=${cartId}&cartItemId=${item.cartItemId}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
        })
            .then(response => {
                fetchInventories(product.productId).then(fetchCart)
            })
    }


    const fetchCart = () => {
        fetchInventories().then(()=>{
            fetch(`http://localhost:8080/viewcart?aggregateId=${cartId}`)
                       .then(response => response.json())
                       .then(response => {
                           setCartItems([...Object.values(response?.cartItems)])
                       })
        })
    }
    const fetchInventories = () => {
        return fetch(`http://localhost:8080/inventory?aggregateId=${cartId}`)
            .then(response => response.json())
            .then(response => {
                var inventorydata = {...inventories, ...response.inventoryMap}
                setInventories(inventorydata)
            })
    }

    return (

        <div>

            <DebugEvents applyFn={(cart) => setCartItems(Object.values(cart?.cartItems))} aggregateId={cartId}/>
            <div className="content container">
                <Head>
                    <title>Challenger</title>
                    <link rel="icon" href="/favicon.ico"/>
                </Head>
                <main>
                    <div className={"canvas content"}>

                        <div>
                            <img src="/assets/images/banner.png"/>
                        </div>
                        <div>
                            <div className={"button is-success"} onClick={() => addCartItem()}>Item hinzufügen
                            </div>
                            <div className={"left-margin button is-success"} onClick={() => fetchCart()}>Refresh
                                                       </div>
                        </div>

                        <div className={"columns"}>
                            <div className={"column"}>
                                <p><b>CartId:</b> {cartId}</p>
                                {cartItems.map((item, cnt) => {
                                    return <div key={cnt} className="cart-item columns">
                                        <div className={"column padding"}>
                                            <img width={"250px"} src={item.productimage} alt={item.productName}/>
                                        </div>
                                        <div className={"column"}>
                                            <div className="details">
                                                <h3>{item.productName}</h3>
                                                <p>ProductId: {item.productId}</p>
                                                <p>Price: ${item.price}</p>
                                                <p>Quantity: {item.quantity}</p>
                                                <p>Verfügbar: {(inventories[item.productId] == 0 ? "Ausverkauft" :inventories[item.productId])  ?? "999"}</p>
                                            </div>
                                            <div>
                                                <a onClick={() => {
                                                    deleteCartItem(item)
                                                }}>Löschen</a>
                                            </div>
                                        </div>
                                    </div>
                                })}
                            </div>

                        </div>


                    </div>
                </main>
            </div>
        </div>

    );
}
