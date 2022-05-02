import json
from multiprocessing.pool import ThreadPool
import requests
from time import time

#The azure URL that is created
applicationUrl = "https://businessinfoswiftly.azurewebsites.net/"
#applicationUrl = "http://127.0.0.1:8080/"
#The application endpoint regardless of where the endpoint is hosted
applicationEndpoint = "businessInfo/addBusinessInfo"
#How many businessInfo dto's you wish to send with per request
chunkLimit = 100
#How many threads you want in the thread pool sending to the app
threadPoolSize = 5
'''
Dto object that will be sent to our custom azure application... usefull to use the json dump API
'''
class BusinessInfoDto:
    def __init__(self, locationId, businessName, neighborhood, zipCode):
        self.locationId = locationId
        self.businessName = businessName
        self.neighborhood = neighborhood
        self.zipCode = zipCode

'''
Reads the given CSV in the format defined here: https://data.sfgov.org/Economy-and-Community/Registered-Business-Locations-San-Francisco/g8m3-pdis/data
and creates the BusinessInfoDto objectst that we care about
'''
def getBusinessInfoDtoList():
    csvFileName = "RegisteredBusinessLocations.csv"
    csvFile = open(csvFileName, 'r')


    #Create the dto from each line
    dtos = []
    for line in csvFile.readlines():
        columns = line.split(',')
        dtos.append(BusinessInfoDto(columns[0], columns[3], columns[23], columns[7]))

    return dtos

'''
Takes a list of dto's that can be very large (read 100's of thousands) and breaks them into 
easily managable chunks so that they can be put into web requests and multi-threaded
'''
def chunkifyData(dtos):
    #Split to only add 1000 dto's at a time. Spring can usually service around 40 threads at a time so thats 40k writes
    chunks = []
    chunk = []
    currentIndex = 0
    while currentIndex < len(dtos):
        chunk.append(dtos[currentIndex])
        currentIndex += 1
        if len(chunk) >= chunkLimit:
            chunks.append(chunk)
            chunk = []

    return chunks

'''
Method used to take all the chunks we wish to send and put them onto a thread pool to push our java code and 
also demonstrate scripting well/fast
'''
def sendRequestChunks(chunks):
    pool = ThreadPool(processes=threadPoolSize)
    sendTimes = pool.map(asyncSendChunk, chunks)

    averageMs = 0
    for time in sendTimes:
        averageMs += time
    averageMs = averageMs / len(sendTimes)

    print("\nThe average time it took to send a chunk of businessInfoDtos of size "+str(chunkLimit)+" was "+str(averageMs)+" milliseconds.")


'''
Callback method for the threading api to use to allow for asynchronous sending of chunks of data into our app
returns time taken to do request in ms
'''
def asyncSendChunk(chunk):
    startMs = round(time()*1000)
    jsonString = json.dumps([ob.__dict__ for ob in chunk])
    headers = {'content-type' : 'application/json'}
    r = requests.post(applicationUrl+applicationEndpoint, headers=headers, data=jsonString)
    if(r.status_code != 202):
        print("Error sending data\n\tResponse code: "+str(r.status_code)+"\n\tReason: "+str(r.reason)+"\n\tJson Sent: "+jsonString)
    else:
        print("Successfully sent "+str(len(chunk))+" rows!")
    endMs = round(time()*1000)
    return endMs - startMs

'''
Main logic of the script:
    Read the csv file and create dto objects
    Seperate them into digestable chunks of a size you set in the script
    Send them on a thread pool asynchronously printing results
'''
def fillBusinessInfo():
    #Read the csv file to get them into our database
    dtos = getBusinessInfoDtoList()
    chunks = chunkifyData(dtos)
    sendRequestChunks(chunks)

if __name__ == "__main__":
    fillBusinessInfo()
