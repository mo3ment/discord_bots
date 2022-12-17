import discord
import random
import asyncio

client = discord.Client()

prefixListOfMessages = ["prefix msg1", "prefix msg2", "prefix msg3"]
suffixListOfMessages = ["suffix msg1", "suffix msg2", "suffix msg3"]
secondsInAMinute = 60
channelId = ""
botToken = ""
randomMessage = random.choice(prefixListOfMessages + suffixListOfMessages)

serverName = discord.utils.escape_markdown("**Tomodachi Pixelmon Server**")

async def send_random_message():
    
    await client.get_channel(channelId).send(randomMessage)
    
    # SLEEP IS MEASURED IN SECONDS: 3600 SEC AN HOUR
    await asyncio.sleep(random.randint(60, 90) * secondsInAMinute)
    
    # RECURSIVELY CALLED; RUNNING INDEFINITELY
    await send_random_message()

@client.event
async def on_ready():
    await send_random_message()

client.change_presence(activity=discord.Activity(type=discord.ActivityType.playing, name=f"on the {serverName}"))
client.run(botToken)