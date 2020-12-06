using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using project_phonebook.DbContexts;
using project_phonebook.Models;
using project_phonebook.ViewModels;

namespace project_phonebook.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ContactListController : ControllerBase
    {
        private readonly PhonebookDbContext _context;
        public ContactListController(PhonebookDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IEnumerable<ContactView> GetContactList()
        {
            var query = from contact in _context.Set<Contact>() select new { contact };
            List<ContactView> contactList = new List<ContactView>();
            foreach (var record in query)
            {
                contactList.Add(new ContactView { 
                    name = record.contact.Name,
                    phone = record.contact.Phone
                });
            }
            return contactList;
        }
    }
}
